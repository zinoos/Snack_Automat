public class PaymentResult {
    private final boolean successful;
    private final String message;
    private final double remainingBalance;

    public PaymentResult(boolean successful, String message, double remainingBalance) {
        this.successful = successful;
        this.message = message;
        this.remainingBalance = remainingBalance;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }
}
