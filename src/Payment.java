public class Payment {
    private double insertedMoney = 0.0;

    public double getInsertedMoney() {
        return insertedMoney;
    }

    public void insertMoney(double amount) {
        if (amount > 0) {
            insertedMoney += amount;
        }
    }

}