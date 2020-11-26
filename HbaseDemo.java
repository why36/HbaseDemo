import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
public class HbaseDemo{
private static Configuration conf = null;
    static{
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "node1");
        conf.set("hbase.zookeeper.property.clientPort","2181")
    }

    public static void createTable(String tableName, String[] families) throws Exception{
        HBaseAdmin admin = new HBaseAdmin(conf);
        if(admin.tableExists(tableName)) {
            System.out.println("table" + tableName + "already exists!");
        } else{
            HTableDescriptor tableDesc = new HColumnDescriptor(tableName);
            for(int i=0; i<families.length; i++){
                tableDesc.addFamily(new HColumnDescriptor(families[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table" + tableName + "success!");
        }
    }

    public static void main(String[] args) {
        try{
            String tablename = "students";
            String[] families = {"ID", "Description", "Courses", "Home"};
            HbaseDemo.createTable(tablename, families);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}