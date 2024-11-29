import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager {
  private ArrayList<User> users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests;

  public double totalRevenue; // Total revenues accumulated via rides and deliveries

  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager() {
    users = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>();

    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);

    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is
  // invalid
  // (e.g. user does not exist, invalid address etc.)
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage() {
    return errMsg;
  }

  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId) {
    // Fill in the code
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).getAccountId().equals(accountId)) {
        return users.get(i);
      }
    }

    return null;
  }

  // Check for duplicate user
  private boolean userExists(User user) {
    // Fill in the code
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).equals(user)) {
        return true;
      }
    }
    return false;
  }

  // Check for duplicate driver
  private boolean driverExists(Driver driver) {
    // Fill in the code
    for (int i = 0; i < drivers.size(); i++) {
      if ((drivers.get(i)).equals(driver)) {
        return true;
      }
    }
    return false;
  }

  // Given a user, check if user ride/delivery request already exists in service
  // requests
  private boolean existingRequest(TMUberService req) {
    // Fill in the code
    for (int i = 0; i < serviceRequests.size(); i++) {
      if ((serviceRequests.get(i)).equals(req)) {
        return true;
      }
    }

    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance
  private double getDeliveryCost(int distance) {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance) {
    return distance * RIDERATE;

  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver() {
    // Fill in the code
    for (int i = 0; i < drivers.size(); i++) {
      if (drivers.get(i).getStatus().equals(Driver.Status.AVAILABLE)) {
        return drivers.get(i);
      }
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers() {
    System.out.println();

    for (int i = 0; i < users.size(); i++) {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers() {
    // Fill in the code
    for (int i = 0; i < drivers.size(); i++) {
      drivers.get(i).printInfo();
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests() {
    // Fill in the code
    for (int i = 0; i < serviceRequests.size(); i++) {
      serviceRequests.get(i).printInfo();
      System.out.println();
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String name, String address, double wallet) {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    // set errMsg string variable to "Invalid Address "
    // return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    if (name == null || name == "") {
      errMsg = "Invalid User Name";
      return false;
    } else if (address == null || address == "" || !(CityMap.validAddress(address))) {
      errMsg = "Invalid User Name";
      return false;
    } else if (wallet < 0) {
      errMsg = "Invalid Money in Wallet";
      return false;
    } else {
      User object = new User(Integer.toString(userAccountId), name, address, wallet);
      if (userExists(object)) {
        errMsg = "User Already Exists in System";
        return false;
      }
      users.add(object);
      userAccountId++;
      return true;
    }
  }

  // Add a new driver to the system
  public boolean registerNewDriver(String name, String carModel, String carLicencePlate) {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    if (name == null || name == "") {
      errMsg = "Invalid Driver Name";
      return false;
    } else if (carModel == null || carModel == "") {
      errMsg = "Invalid Car Model";
      return false;
    } else if (carLicencePlate == null || carLicencePlate == "") {
      errMsg = "Invalid Car Licence Plate";
      return false;
    } else {
      Driver object = new Driver(Integer.toString(driverId), name, carModel, carLicencePlate);
      if (driverExists(object)) {
        return false;
      }
      drivers.add(object);
      driverId++;
      return true;
    }
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to) {
    // Check for valid parameters
    // Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user
    // at a time!
    // Change driver status
    // Add the ride request to the list of requests
    // Increment the number of rides for this user
    int val = 0;

    for (int i = 0; i < users.size(); i++) {
      if ((users.get(i).getAccountId().equals(accountId))) {

        val = i + 1;
        break;
      }
    }
    if (val == 0) {
      errMsg = "User Account Not Found";
      return false;
    } else if (CityMap.getDistance(from, to) < 1) {
      errMsg = "Insufficient Travel Distance";
      return false;
    } else if (!(CityMap.validAddress(from)) || !(CityMap.validAddress(to))) {
      errMsg = "Invalid User Address ";
      return false;
    }

    else {
      Driver person = getAvailableDriver();
      double cost = getDeliveryCost(CityMap.getDistance(from, to));
      TMUberRide delivery = new TMUberRide(driver, from, to, users.get(val), CityMap.getDistance(from, to), cost);
      if (person == null) {
        errMsg = "No Drivers Available";
        return false;
      } else if (cost > users.get(val).getWallet()) {
        errMsg = "Insufficient Funds";
        return false;
      }

      else if (existingRequest(delivery)) {
        errMsg = "User Already Has Delivery Request at Restaurant with this Food Order";
        return false;
      }
      person.setStatus(Driver.Status.DRIVING);
      serviceRequests.add(delivery);
      users.get(val).addRide();
      return true;

    }
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId) {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food
    // order id
    // Increment the number of deliveries the user has had
    User user = getUser(accountId);
    if (user == null) {
      errMsg = "User Account Not Found";
      return false;
    } else if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
      errMsg = "Invalid User Address";
      return false;
    } else if (CityMap.getDistance(from, to) < 1) {
      errMsg = "Insufficient Travel Distance";
      return false;
    } else {
      Driver driver = getAvailableDriver();
      double cost = getDeliveryCost(CityMap.getDistance(from, to));
      TMUberDelivery delivery = new TMUberDelivery(driver, from, to, user, CityMap.getDistance(from, to), cost,
          restaurant, foodOrderId);
      if (driver == null) {
        errMsg = "No Drivers Available";
        return false;
      } else if (cost > user.getWallet()) {
        errMsg = "Insufficient Funds";
        return false;
      } else if (existingRequest(delivery)) {
        errMsg = "User Already Has a Delivery Request";
        return false;
      }
      driver.setStatus(Driver.Status.DRIVING);
      serviceRequests.add(delivery);
      user.addDelivery();
      return true;
    }
  }

  // Cancel an existing service request.
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request) {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    if ((request - 1) < serviceRequests.size()) {
      TMUberService myreq = serviceRequests.get(request - 1);
      serviceRequests.remove(myreq);

      if (myreq.getServiceType().equalsIgnoreCase("RIDE")) {
        myreq.getUser().deDelivery();
        ;
        return true;
      } else {
        myreq.getUser().deDelivery();
        ;
        return true;
      }
    }
    errMsg = "Invalid Request #"; // Print invalid request if request not found.
    return false;
  }

  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request) {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    if (request - 1 >= serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
    } else {
      double mycost = serviceRequests.get(request - 1).getCost();
      totalRevenue += mycost;

      Driver myDriver1 = serviceRequests.get(request - 1).getDriver();
      myDriver1.pay((myDriver1.getWallet() + (PAYRATE * mycost)));
      ;
      myDriver1.setStatus(Driver.Status.AVAILABLE);

      User myUser1 = serviceRequests.get(request - 1).getUser();
      myUser1.payForService(mycost);
      totalRevenue -= PAYRATE * mycost;
      serviceRequests.remove(request - 1);

    }
    return true;
  }

  // Sort users by name
  // Then list all users
  public void sortByUserName() {
    Collections.sort(users, new NameComparator());
    listAllUsers();
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
      return user1.getName().compareTo(user2.getName());

    }

  }

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet() {
    Collections.sort(users, new UserWalletComparator());
    listAllUsers();
  }

  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User> {
    public int compare(User u1, User u2) {
      return Double.compare(u1.getWallet(), u2.getWallet());
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance() {
    Collections.sort(serviceRequests, new DistanceComparator());
    listAllServiceRequests();
  }

  private class DistanceComparator implements Comparator<TMUberService> {
    @Override
    public int compare(TMUberService service1, TMUberService service2) {
      return Integer.compare(service1.getDistance(), service2.getDistance());
    }
  }
}
