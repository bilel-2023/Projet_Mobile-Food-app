import java.util.List;

public class Instruction {
    private String name; // Optional, for instruction section name
    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }

    public static class Step {
        private int number;
        private String step;

        public String getStep() {
            return step;
        }

        public int getNumber() {
            return number;
        }
    }
}
