package nju;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
 
import java.io.IOException;
public class HbaseDemo {
    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;
    public static void main(String[] args)throws IOException{
        init();
        createTable("students",new String[]{"ID", "Description", "Courses", "Home"});
        insertData("students", "001", "Description", "Name", "Li Lei");
        insertData("students", "001", "Description", "Height", "176");
        insertData("students", "001", "Courses", "Chinese", "80");
        insertData("students", "001", "Courses", "Math", "90");
        insertData("students", "001", "Courses", "Physics", "95");
        insertData("students", "001", "Home", "Province", "Zhejiang");
        insertData("students", "002", "Description", "Name", "Han Meimei");
        insertData("students", "002", "Description", "Height", "183");
        insertData("students", "002", "Courses", "Chinese", "88");
        insertData("students", "002", "Courses", "Math", "77");
        insertData("students", "002", "Courses", "Physics", "66");
        insertData("students", "002", "Home", "Province", "Beijing");
        insertData("students", "003", "Description", "Name", "Xiao Ming");
        insertData("students", "003", "Description", "Height", "162");
        insertData("students", "003", "Courses", "Chinese", "90");
        insertData("students", "003", "Courses", "Math", "90");
        insertData("students", "003", "Courses", "Physics", "90");
        insertData("students", "003", "Home", "Province", "Shanghai");
        scanTable("students");
        getData("students", "001", "Home","Province");
        insertData("students", "003", "Courses", "English", "100");
        addNewColumnFamily("students", "Contact");
        insertData("students", "003", "Contact", "Email", "123@nju.com");
        scanTable("students");
        deleteTable("students");
        close();
    }
 
    public static void init(){
        configuration  = HBaseConfiguration.create();
        configuration.set("hbase.rootdir","hdfs://localhost:9000/hbase");
        try{
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
 
    public static void close(){
        try{
            if(admin != null){
                admin.close();
            }
            if(null != connection){
                connection.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
 
    public static void createTable(String myTableName,String[] colFamily) throws IOException {
        System.out.println("Start creating new Table: " + myTableName);
        TableName tableName = TableName.valueOf(myTableName);
        if(admin.tableExists(tableName)){
            System.out.println("talbe is exists!");
        }else {
            TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(tableName);
            for(String str:colFamily){
                ColumnFamilyDescriptor family = 
ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(str)).build();
                tableDescriptor.setColumnFamily(family);
            }
            admin.createTable(tableDescriptor.build());
        } 
    }

    public static void deleteTable(String myTableName) throws IOException{
        TableName tableName = TableName.valueOf(myTableName);
        try {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("Delete Table: " + myTableName + "success !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void insertData(String tableName,String rowKey,String colFamily,String col,String val) throws IOException { 
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(rowKey.getBytes());
        put.addColumn(colFamily.getBytes(),col.getBytes(), val.getBytes());
        table.put(put);
        table.close(); 
    }
 
    public static void getData(String tableName,String rowKey,String colFamily, String col) throws  IOException{
        System.out.println("Start getting data... "); 
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowKey.getBytes());
        get.addColumn(colFamily.getBytes(),col.getBytes());
        Result result = table.get(get);
        System.out.println(rowKey + "'s home province is: " + new String(result.getValue(colFamily.getBytes(),col==null?null:col.getBytes())));
        table.close(); 
    }

    public static void scanTable(String tableName) throws  IOException{
        System.out.println("Start scanning Table: " + tableName);
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        try{
            for(Result row:results) {
                for (Cell cell : row.listCells()) {
                    System.out.println(
                        "RowKey:"
                        + Bytes.toString(row.getRow())
                        + " Family:"
                        + Bytes.toString(CellUtil.cloneFamily(cell))
                        + " Qualifier:"
                        + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + " Value:"
                        + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
            table.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addNewColumnFamily(String tableName,String colFamily) throws  IOException{
        try{
            System.out.println("Start adding new column family: " + colFamily);
            TableName table = TableName.valueOf(tableName);
            admin.disableTable(table);
            HColumnDescriptor hcd = new HColumnDescriptor(colFamily); //新增列族
            admin.addColumn(table, hcd);
            admin.enableTableAsync(table);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}