package com.company.gui;


import com.company.bean.AnswerSheet;
import com.company.bean.TestPaper;
import com.company.gui.my_component.MyButton;
import com.company.gui.my_component.MyPanel;
import com.company.gui.my_component.MyScrollPane;
import com.company.utils.Consts;
import javafx.scene.chart.NumberAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.List;

public class QueryFrame extends BaseFrame{
    private ClientContext context;
    private TestPaper paper;

    private MyPanel pane,infoPanel,tablePanel,chartPanel;
    private MyScrollPane scrollTable;
    private JLabel info;
    private JTable table;

    private DefaultCategoryDataset dataset=new DefaultCategoryDataset();

    private Object[] tableTitle;
    private Object[][] tableData;

    public QueryFrame(){init();}
    public QueryFrame(ClientContext context,TestPaper paper){
        this();
        this.context=context;
        setPaper(paper);
        fillData();
    }
    private void fillData(){
        List<AnswerSheet> list=context.getCorrected(paper);
        double average=0;
        for(AnswerSheet sheet:list)average+=context.getSheetScore(sheet);
        average/=list.size();
        info.setText("id："+paper.getId()+
                "     考试名："+paper.getTitle()+
                "     总分："+context.getScoreOfType(paper,Consts.TYPE_ALL)+
                "     平均分："+average);
        tableData=context.getTableData(paper);
        if(tableData.length==0){
            JOptionPane.showMessageDialog(this,"尚不存在完成考试的学生！");
            return;
        }
        fillTable();
        fillChart();
    }
    private void fillChart(){
        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        List<AnswerSheet> list=context.getCorrected(paper);
        int[] range=new int[10];
        double fullMark=context.getScoreOfType(paper,Consts.TYPE_ALL);
        for(AnswerSheet sheet:list){
            double score=context.getSheetScore(sheet);
            for(int i=9;i>=0;i--)
                if(score*10>=(i)*fullMark&&score*10<=(i+1)*fullMark){range[i]++;break;}
        }
        int allNum=list.size();
        for(int i=0;i<10;i++)
            dataset.addValue((double)range[i]/allNum,"",(i*10)+"%~"+((i+1)*10)+"%");


        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        standardChartTheme.setExtraLargeFont(new Font("宋书", Font.BOLD, 10));
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 10));
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 10));
        ChartFactory.setChartTheme(standardChartTheme);

        JFreeChart chart= ChartFactory.createBarChart3D("各分数段成绩统计","分数段","人数",
                dataset, PlotOrientation.VERTICAL,false,false,false);

        CategoryPlot plot = chart.getCategoryPlot();
        ValueAxis numAxis = plot.getRangeAxis();
        numAxis.setVisible(false);
        BarRenderer3D renderer = new BarRenderer3D();
        //renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}" ,
                                DecimalFormat.getPercentInstance()));
        renderer.setItemLabelFont(new Font( "黑体 ",Font.PLAIN,10));
        renderer.setItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
        plot.setRenderer(renderer);


        ChartPanel chartPane=new ChartPanel(chart);
        chartPane.setBounds(0,0,WW(17),HH(25));

        chartPanel.add(chartPane);

    }
    private void fillTable(){
        tableTitle=new Object[paper.getNumOfQuestions()+4];
        tableTitle[0]="排名";tableTitle[1]="姓名";tableTitle[2]="学号";tableTitle[3]="总分";
        for(int i=4;i<paper.getNumOfQuestions()+4;i++)tableTitle[i]="问题"+paper.getQuestionIdByRank(i-4);


        DefaultTableModel model=new DefaultTableModel(tableData,tableTitle);

        table=new JTable(model);
        table.setEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(HH(1));


        scrollTable=new MyScrollPane(table);
        scrollTable.setBounds(0,0,WW(11),HH(25));

        tablePanel.add(scrollTable);
    }
    protected void init() {

        ImageIcon icon=new ImageIcon("src/com/company/img/query.jpg" );

        JLabel backGround=new JLabel(icon);
        backGround.setBounds(0,0,WIDTH,HEIGHT);
        JPanel myPanel = (JPanel)this.getContentPane();		//把我的面板设置为内容面板
        myPanel.setOpaque(false);					//把我的面板设置为不可视
        myPanel.setLayout(null);		//把我的面板设置为流动布局
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(backGround, new Integer(Integer.MIN_VALUE));
        
        this.setSize(WIDTH,HEIGHT);
        this.setLocation(0,0);
        this.setTitle("成绩统计");
        this.add(createPanel());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                context.exit();
            }
        });
    }
    private MyPanel createPanel(){
            pane=new MyPanel();
            pane.setSize(WIDTH,HEIGHT);
            pane.setOpaque(false);//透明
            pane.setLayout(null);

            createInfo();
            createTable();
            createChart();

            MyButton back=new MyButton("返回");
            back.setBounds(0,0,WW(1),HH(1));
            back.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {context.getToCorrect(paper);context.backToTeacherMenu();}});
            pane.add(back);

            return pane;
    }

    private void createInfo(){
        infoPanel=new MyPanel();
        infoPanel.setBounds(WW(1),HH(1),WW(28),HH(2));
        infoPanel.setLayout(null);
        infoPanel.setOpaque(false);

        info=new JLabel("id：考试名：平均分：",JLabel.CENTER);
        info.setBounds(0,0,WW(28),HH(2));
        infoPanel.add(info);

        pane.add(infoPanel);
    }
    private void createChart(){
        chartPanel=new MyPanel();
        chartPanel.setBounds(WW(1),HH(3),WW(17),HH(25));
        chartPanel.setLayout(null);
        chartPanel.setOpaque(false);

        pane.add(chartPanel);
    }
    private void createTable(){
        tablePanel=new MyPanel();
        tablePanel.setBounds(WW(18),HH(3),WW(11),HH(25));
        tablePanel.setLayout(null);
        tablePanel.setOpaque(false);

        pane.add(tablePanel);
    }

    public void setPaper(TestPaper paper){this.paper=paper;}
}
