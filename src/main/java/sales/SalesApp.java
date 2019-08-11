package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {
    private SalesDao salesDao;
    private SalesReportDao salesReportDao;

    public SalesApp() {
        salesDao = new SalesDao();
        salesReportDao = new SalesReportDao();
    }

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

        if (!isSalesIdValid(salesId)) return;

        Sales sales = getSalesBySalesId(salesId);

        if (isSalesOutOfEffectiveDate(sales)) {
            return;
        }

        List<SalesReportData> reportDataList = getSalesReportDataBySales(sales);

        List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();

        for (SalesReportData data : reportDataList) {
            if ("SalesActivity".equalsIgnoreCase(data.getType())) {
                if (data.isConfidential()) {
                    if (isSupervisor) {
                        filteredReportDataList.add(data);
                    }
                } else {
                    filteredReportDataList.add(data);
                }
            }
        }

        List<SalesReportData> tempList = new ArrayList<SalesReportData>();
        for (int i = 0; i < reportDataList.size() || i < maxRow; i++) {
            tempList.add(reportDataList.get(i));
        }
        filteredReportDataList = tempList;

        List<String> headers = getReportHeaders(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, reportDataList);

        EcmService ecmService = new EcmService();
        ecmService.uploadDocument(report.toXml());

    }

    protected List<String> getReportHeaders(boolean isNatTrade) {
        if (isNatTrade) {
            return Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
        }
        return Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
    }

    protected boolean isSalesIdValid(String salesId) {
        return salesId != null;
    }

    protected boolean isSalesOutOfEffectiveDate(Sales sales) {
        Date today = new Date();
        return today.after(sales.getEffectiveTo())
                || today.before(sales.getEffectiveFrom());
    }

    protected List<SalesReportData> getSalesReportDataBySales(Sales sales) {
        return salesReportDao.getReportData(sales);
    }

    protected Sales getSalesBySalesId(String salesId) {
        return salesDao.getSalesBySalesId(salesId);
    }

    protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }

}
