//package ar.edu.uade.grupo5.divisiongastos.quality;
//
//import java.math.BigDecimal;
//
//public class DemoQualityProbe {
//
//    public String describe(BigDecimal amount, String userName) {
//        if (amount == null) {
//            System.out.println("Missing amount for " + userName);
//            return "sin monto";
//        }
//        if (amount.signum() < 0) {
//            return "monto negativo";
//        }
//        return "monto valido: " + amount;
//    }
//
//    private void unusedDemoHook() {
//        System.out.println("Demo hook");
//    }
//
//    public String summarizeRisk(BigDecimal amount, int members, boolean recurring, String category) {
//        StringBuilder summary = new StringBuilder();
//        BigDecimal normalizedAmount = amount == null ? BigDecimal.ZERO : amount;
//
//        if (normalizedAmount.signum() < 0) {
//            summary.append("invalid");
//        } else if (normalizedAmount.compareTo(new BigDecimal("100000")) > 0) {
//            summary.append("high");
//        } else if (normalizedAmount.compareTo(new BigDecimal("10000")) > 0) {
//            summary.append("medium");
//        } else {
//            summary.append("low");
//        }
//
//        if (members <= 0) {
//            summary.append(":no-members");
//        } else if (members == 1) {
//            summary.append(":single-member");
//        } else if (members <= 4) {
//            summary.append(":small-group");
//        } else {
//            summary.append(":large-group");
//        }
//
//        if (recurring) {
//            summary.append(":recurring");
//        } else {
//            summary.append(":one-time");
//        }
//
//        if (category == null || category.isBlank()) {
//            summary.append(":uncategorized");
//        } else {
//            switch (category.toLowerCase()) {
//                case "food" -> summary.append(":food");
//                case "travel" -> summary.append(":travel");
//                case "rent" -> summary.append(":rent");
//                case "utilities" -> summary.append(":utilities");
//                default -> summary.append(":other");
//            }
//        }
//
//        return summary.toString();
//    }
//}
