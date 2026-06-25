package algorithms;

public class LIS {

    /**
     * Finds the length of the Longest Increasing Subsequence in an array of prices.
     * Used to detect the longest continuous upward trend (bull run) in historical data.
     */
    public static int longestIncreasingSubsequence(double[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }

        int n = prices.length;
        int[] dp = new int[n];
        int maxLIS = 1;

        // Initialize LIS values for all indexes as 1
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }

        // Compute optimized LIS values in bottom-up manner
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (prices[i] > prices[j] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                }
            }
            if (dp[i] > maxLIS) {
                maxLIS = dp[i];
            }
        }

        return maxLIS;
    }
}
