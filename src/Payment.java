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

    public boolean hasEnoughMoney(double productPrice) {
        return insertedMoney >= productPrice;
    }

    public double completePurchase(double productPrice) {
        double change = insertedMoney - productPrice;
        insertedMoney = 0.0;
        return change;
    }

    public double cancelPurchase() {
        double change = insertedMoney;
        insertedMoney = 0.0;
        return change;
    }

}