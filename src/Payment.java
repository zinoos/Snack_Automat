public class Payment {
    private double insertedMoney = 0.0;

    public double getInsertedMoney() {
        return insertedMoney;
    }

<<<<<<< HEAD
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
=======
    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        insertedMoney += amount;
    }

    public boolean hasEnoughFunds(Product product) {
        return product != null && insertedMoney >= product.getPrice();
    }

    public PaymentResult purchase(Product product) {
        if (product == null) {
            return new PaymentResult(false, "Product not found.", insertedMoney);
        }

        if (!product.isInStock()) {
            return new PaymentResult(false, product.getName() + " is out of stock.", insertedMoney);
        }

        if (!hasEnoughFunds(product)) {
            double needed = product.getPrice() - insertedMoney;
            return new PaymentResult(false, String.format("Add $%.2f more for %s.", needed, product.getName()), insertedMoney);
        }

        insertedMoney -= product.getPrice();
        product.decreaseStock();

        return new PaymentResult(
                true,
                String.format("Dispensing %s.", product.getName()),
                insertedMoney
        );
    }

    public double refund() {
        double refundedAmount = insertedMoney;
        insertedMoney = 0.0;
        return refundedAmount;
    }

    public void reset() {
        insertedMoney = 0.0;
    }
}
>>>>>>> f345c1f (Added Payment GUI elements)
