// package ar.edu.uade.grupo5.divisiongastos.quality;
//
// import org.junit.jupiter.api.Test;
//
// import java.math.BigDecimal;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// class DemoQualityProbeTest {
//
//     private final DemoQualityProbe probe = new DemoQualityProbe();
//
//     @Test
//     void describesMissingAmount() {
//         assertThat(probe.describe(null, "Ana")).isEqualTo("sin monto");
//     }
//
//     @Test
//     void describesNegativeAmount() {
//         assertThat(probe.describe(new BigDecimal("-1.00"), "Ana")).isEqualTo("monto negativo");
//     }
//
//     @Test
//     void describesValidAmount() {
//         assertThat(probe.describe(new BigDecimal("25.50"), "Ana")).isEqualTo("monto valido: 25.50");
//     }
//
//     @Test
//     void summarizesInvalidUncategorizedExpense() {
//         assertThat(probe.summarizeRisk(new BigDecimal("-1.00"), 0, false, null))
//                 .isEqualTo("invalid:no-members:one-time:uncategorized");
//     }
//
//     @Test
//     void summarizesHighRecurringFoodExpense() {
//         assertThat(probe.summarizeRisk(new BigDecimal("100001.00"), 1, true, "food"))
//                 .isEqualTo("high:single-member:recurring:food");
//     }
//
//     @Test
//     void summarizesMediumTravelExpense() {
//         assertThat(probe.summarizeRisk(new BigDecimal("10001.00"), 4, false, "travel"))
//                 .isEqualTo("medium:small-group:one-time:travel");
//     }
//
//     @Test
//     void summarizesLowRentExpenseForLargeGroup() {
//         assertThat(probe.summarizeRisk(new BigDecimal("100.00"), 5, true, "rent"))
//                 .isEqualTo("low:large-group:recurring:rent");
//     }
//
//     @Test
//     void summarizesUtilitiesAndOtherCategories() {
//         assertThat(probe.summarizeRisk(BigDecimal.ZERO, 2, false, "utilities"))
//                 .isEqualTo("low:small-group:one-time:utilities");
//         assertThat(probe.summarizeRisk(BigDecimal.ZERO, 2, false, "parking"))
//                 .isEqualTo("low:small-group:one-time:other");
//     }
// }
