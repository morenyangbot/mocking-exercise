package sales;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

    @InjectMocks
    SalesApp salesApp;
    @Mock
    SalesDao salesDao;
    @Mock
    SalesReportDao salesReportDao;

//	@Test
//	public void testGenerateReport() {
//
//		SalesApp salesApp = new SalesApp();
//		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
//
//	}


    @Test
    public void testIsSalesOutOfEffectiveDate_givenEffectiveStartDateIsYesterdayAndEffectiveEndDateIsTomorrow_thenReturnFalse() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);

        Sales sales = mock(Sales.class);
        when(sales.getEffectiveFrom()).thenReturn(yesterday.getTime());
        when(sales.getEffectiveTo()).thenReturn(tomorrow.getTime());

        boolean isOutOfEffectiveDate = salesApp.isSalesOutOfEffectiveDate(sales);

        assertFalse(isOutOfEffectiveDate);
    }

    @Test
    public void testIsSalesOutOfEffectiveDate_givenEffectiveStartDateIsTheDayBeforeYesterdayAndEffectiveEndDateIsYesterday_thenReturnTrue() {
        Calendar theDayBeforeYesterday = Calendar.getInstance();
        theDayBeforeYesterday.add(Calendar.DATE, -2);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Sales sales = mock(Sales.class);
        when(sales.getEffectiveFrom()).thenReturn(theDayBeforeYesterday.getTime());
        when(sales.getEffectiveTo()).thenReturn(yesterday.getTime());

        boolean isOutOfEffectiveDate = salesApp.isSalesOutOfEffectiveDate(sales);

        assertTrue(isOutOfEffectiveDate);
    }

    @Test
    public void testIsSalesOutOfEffectiveDate_givenEffectiveStartDateIsTomorrowAndEffectiveEndDateIsTheDayAfterTomorrow_thenReturnTrue() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);

        Calendar theDayAfterTomorrow = Calendar.getInstance();
        theDayAfterTomorrow.add(Calendar.DATE, 2);

        Sales sales = mock(Sales.class);
        when(sales.getEffectiveFrom()).thenReturn(tomorrow.getTime());
        when(sales.getEffectiveTo()).thenReturn(theDayAfterTomorrow.getTime());

        boolean isOutOfEffectiveDate = salesApp.isSalesOutOfEffectiveDate(sales);

        assertTrue(isOutOfEffectiveDate);
    }
}
