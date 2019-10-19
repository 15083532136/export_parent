package cn.itcast.web.controller.vo;

import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.DownloadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/18 8:24
 * @description：出货表
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractProductVoController extends BaseController {

    @Autowired
    private DownloadUtil downloadUtil;
    @Reference
    private ContractProductService contractProductService;

    @RequestMapping("/print")
    public String print(){
        return "cargo/print/contract-print";
    }

    /**
     * 出货表打印
     * @Method: printExcel
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param inputDate
     * @Return void
     * @Exception
     * @Date 2019/10/18 8:33
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //创建第一行
        Row firstRow = sheet.createRow(0);
        // 设置列宽
        sheet.setColumnWidth(0,256*5);
        sheet.setColumnWidth(1,256*26);
        sheet.setColumnWidth(2,256*11);
        sheet.setColumnWidth(3,256*29);
        sheet.setColumnWidth(4,256*15);
        sheet.setColumnWidth(5,256*10);
        sheet.setColumnWidth(6,256*30);
        sheet.setColumnWidth(7,256*30);
        sheet.setColumnWidth(8,256*10);

        String[] inputStr = inputDate.split("-");
        String month = "";
        //如果数组第二个字符是以0开头，去除0
        if (inputStr[1].startsWith("0")){
            month = inputStr[1].substring(1);
        }
        String title = inputStr[0]+"年"+month+"月份出货表";
        //合并单元格
        Row row = sheet.createRow(0);
        row.setHeightInPoints(36);
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,9));
        //设置大标题样式
        Cell cell = row.createCell(1);
        cell.setCellStyle(this.bigTitle(workbook));
        cell.setCellValue(title);

        //设置第二行的数据和样式
        Row secRow = sheet.createRow(1);
        secRow.setHeightInPoints(26.3f);
        String[] header =  {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i < 8; i++) {
            Cell secRowCell = secRow.createCell(i + 1);
            secRowCell.setCellStyle(title(workbook));
            secRowCell.setCellValue(header[i]);
        }
        //设置第三行的数据和样式
        List<ContractProductVo> productVoList = contractProductService.findByShipTime(getLoginCompanyId(),inputDate);
        if (productVoList!=null&&productVoList.size()>0){
            int rows = 2;
            for (int j = 0; j < 5000; j++) {
                for (int index = 0; index < productVoList.size(); index++) {
                        Row voRow = sheet.createRow(rows++);
                    for (int i = 0; i < 8; i++) {
                        Cell voRowCell = voRow.createCell(i + 1);
                        //设置单元格样式
                        //voRowCell.setCellStyle(title(workbook));
                        ContractProductVo vo = productVoList.get(index);
                        if (i==0){
                            voRowCell.setCellValue(vo.getCustomName()!=null?vo.getCustomName():"");
                        }
                        else if (i==1){
                            voRowCell.setCellValue(vo.getContractNo()!=null?vo.getContractNo():"");
                        }
                        else if (i==2){
                            voRowCell.setCellValue(vo.getContractNo()!=null?vo.getContractNo():"");
                        }
                        else if (i==3){
                            voRowCell.setCellValue(vo.getCnumber()!=null?vo.getCnumber()+"":"");
                        }
                        else if (i==4){
                            voRowCell.setCellValue(vo.getFactoryName()!=null?vo.getFactoryName():"");
                        }
                        else if (i==5){
                            String strDate = getStrDate(vo.getDeliveryPeriod());
                            voRowCell.setCellValue(strDate!=null?strDate:"");
                        }
                        else if (i==6){
                            String dateStr = getStrDate(vo.getShipTime());
                            voRowCell.setCellValue(dateStr!=null?dateStr+"":"");
                        }
                        else if (i==7){
                            voRowCell.setCellValue(vo.getTradeTerms()!=null?vo.getTradeTerms():"");
                        }
                    }
                }
            }
        }
        //输出流下载Excel文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        downloadUtil.download(bos,response,title+".xls");

    }

    private String getStrDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
        if (date!=null){
            String dateStr = sdf.format(date);
            return dateStr;
        }
        return null;
    }

    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }
}
