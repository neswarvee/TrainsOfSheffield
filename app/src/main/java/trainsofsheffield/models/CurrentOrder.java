//model class for CurrentOrder that sets the getters and setter methods
package trainsofsheffield.models;
import java.util.*;
    public class CurrentOrder {
        private int userID;
        private List<String> orders;

        public CurrentOrder(int userID) {
            this.userID = userID;
            this.orders = new ArrayList<>();
        }

        public void addOrder(String order) {
            orders.add(order);
        }

        public List<String> getOrders() {
            return orders;
        }

        public void setOrders(List<String> orders) {
            this.orders = orders;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }
    }

