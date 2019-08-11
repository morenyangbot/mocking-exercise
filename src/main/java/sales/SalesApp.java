package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SalesApp {
    private SalesDao salesDao;
    private SalesReportDao salesReportDao;
    private EcmService ecmService;

    public SalesApp() {
        salesDao = new SalesDao();
        salesReportDao = new SalesReportDao();
        ecmService = new EcmService();
    }

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

        if (!isSalesIdValid(salesId)) return;

        Sales sales = getSalesBySalesId(salesId);

        if (isSalesOutOfEffectiveDate(sales)) {
            return;
        }

        List<SalesReportData> reportDataList = getSalesReportDataBySales(sales);

        List<SalesReportData> filteredReportDataList = getFilteredReportDataList(isSupervisor, reportDataList);

        filteredReportDataList = getLimitedSalesReportData(maxRow, reportDataList);

        List<String> headers = getReportHeaders(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, reportDataList);

        uploadReportAsXml(report);

    }

    protected List<SalesReportData> getLimitedSalesReportData(int maxRow, List<SalesReportData> reportDataList) {
        List<SalesReportData> tempList = new ArrayList<SalesReportData>();
        // the origin logic was wrong and may case IndexOutOfBoundsException, so I change || to &&
        for (int i = 0; i < reportDataList.size() && i < maxRow; i++) {
            tempList.add(reportDataList.get(i));
        }
        return tempList;
    }

    protected List<SalesReportData> getFilteredReportDataList(boolean isSupervisor, List<SalesReportData> reportDataList) {
        return reportDataList.stream()
                .filter(salesReportData -> isSalesReportDataValid(isSupervisor, salesReportData))
                .collect(Collectors.toList());
    }

    protected boolean isSalesReportDataValid(boolean isSupervisor, SalesReportData data) {
        final String ALLOWED_TYPE = "SalesActivity";
        return ALLOWED_TYPE.equalsIgnoreCase(data.getType()) && (!data.isConfidential() || isSupervisor);
    }

    protected void uploadReportAsXml(SalesActivityReport report) {
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
