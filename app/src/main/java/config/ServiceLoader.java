package config;
import com.wangpos.test.inter.*;



import java.util.HashMap;
import java.util.Map;


/**
 * Created by qiyue on 2018/5/30.
 */

public class ServiceLoader {

	Map<Object,Object> configs = new HashMap<>();


	public ServiceLoader(){
		configs.put(InstallManager.class,InstallManagerImpl.class);
		configs.put(InstallManager.class,InstallManagerImpl.class);
		configs.put(InstallManager.class,InstallManagerImpl.class);
		configs.put(InstallManager.class,InstallManagerImpl.class);
		configs.put(InstallManager.class,InstallManagerImpl.class);


	}

}
