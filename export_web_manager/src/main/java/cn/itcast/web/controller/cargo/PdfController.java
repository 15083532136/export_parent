package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.BeanMapUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/21 9:47
 * @description：pdf
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {

    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 报表pdf下载
     * @throws Exception
     */
    @RequestMapping("/exportPdf")
    public void exportPdf(String id) throws Exception {
        InputStream is =
                session.getServletContext().
                        getResourceAsStream("/jasper/export.jasper");
        //1.出口报运
        Export export = exportService.findById(id);
        Map<String, Object> exportMap = BeanMapUtils.beanToMap(export);
        //2.报运商品
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        ServletOutputStream outputStream = response.getOutputStream();
        //设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
        response.setContentType("application/pdf;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;fileName=export-"+id+".pdf");
        JasperPrint jasperPrint
                = JasperFillManager.fillReport(is,exportMap,dataSource);
        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
        outputStream.close();
    }
}
