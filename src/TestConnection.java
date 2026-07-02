public class TestConnection {

    public static void main(String[] args) {

        // Call DB connection method
        if (DBConnection.getConnection() != null) {
            System.out.println("Connected to MySQL ✅");
        } else {
            System.out.println("Connection Failed ❌");
        }

    }
}