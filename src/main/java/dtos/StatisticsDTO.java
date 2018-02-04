package dtos;

public class StatisticsDTO {

    //sum is a double specifying the total sum of transaction value in the last 60 seconds
    private double sum;

    //avg is a double specifying the average amount of transaction value in the last 60 seconds
    private double avg;

    //max is a double specifying single highest transaction value in the last 60 seconds
    private double max;

    //min is a double specifying single lowest transaction value in the last 60 seconds
    private double min;

    //count is a long specifying the total number of transactions happened in the last 60 seconds
    private double count;

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getCount() {
        return count;
    }
}
