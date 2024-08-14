package generators;
import java.rmi.server.UID;

public class UIDGenerator {
	public static String nextUID() {
		return new UID().toString();
	}

}
