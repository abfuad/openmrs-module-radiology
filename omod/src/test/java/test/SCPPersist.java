//package test;
//
//import java.sql.SQLException;
//
//import com.hxti.edge.pacs.data.ServiceClassProvider;
//import com.hxti.edge.pacs.util.sql.SqlClientContainer;
//
//public class SCPPersist
//{
//
//	public static void main(String[] args)
//	{
//		String archiveDirectory="/tmp/testSCPpersist";
//		try
//      {
//	      new SCPPersist().saveSCP("RAD",archiveDirectory,11112,archiveDirectory+"/tmp");
//      }
//      catch(SQLException e)
//      {
//	      e.printStackTrace();
//      }
//
//	}
//	
//	public void saveSCP(String aeTitle, String archiveDirectory, int port, String tmpDirectory) throws SQLException{
//		ServiceClassProvider scp= new ServiceClassProvider();
//		scp.setAETitle(aeTitle);
//		scp.setArchiveDirectory(archiveDirectory);
//		scp.setPort(port);
//		scp.setTmpDirectory(tmpDirectory);
//		SqlClientContainer.sql.insert("persistSCP",scp);
//	}
//
//}
