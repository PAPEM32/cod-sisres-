package com.sisres.report;

import java.util.logging.Level;

import javax.servlet.ServletContext;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

public class BirtEngine {
	// TODO: P32 Config variavel ambiente ou resource
	// private static final String BIRT_SRV_HMLG_BANCADA =
	// "/jboss/JBoss/birt-runtime-2_3_2/ReportEngine/";
	//private static final String BIRT_SRV_HMLG_BANCADA = "/home/papem-3201/Apps/libsisres/birt-runtime-2_3_2/ReportEngine/";
	// private static final String BIRT_SRV_VIRTUAL_PRD_E_HMLG =
	// "/usr/local/birt-runtime-2_3_2/ReportEngine/";
	// private static final String BIRT_SRV_VIRTUAL_PRD_E_HMLG =
	// "//home/papem-3201/Apps/libsisres/birt-runtime-2_3_2/ReportEngine/";
	// private static final String BIRT_MAQUINA_DESENV_LOCAL =
	// "C:/jboss-5.0.1.GA/birt-runtime-2_3_2/ReportEngine";
	private static IReportEngine birtEngine = null;

	// private static Properties configProps = new Properties();

	// private final static String configFile =
	// "C:/Users/INETEP/IBM/rationalsdp/workspace/birtSisres/WebContent/WEB-INF/classes/engine/BirtConfig.properties";

	public static synchronized IReportEngine getBirtEngine(ServletContext sc) {
		if (birtEngine == null) {

			EngineConfig config = new EngineConfig();

			// para funcionar no Linux --> Este caminho deverá ser modificado quando a nova
			// máquina virtual de homologação estiver no ar
			// caminho para servidor virtual
			// config.setEngineHome(BIRT_SRV_VIRTUAL_PRD_E_HMLG);
			// caminho para servidor de bancada
			// TODO: p32 verificar
			// config.setEngineHome(BIRT_SRV_HMLG_BANCADA);
			// para funcionar no windows - local
			// config.setEngineHome(BIRT_MAQUINA_DESENV_LOCAL);

			/*
			 * if( configProps != null){ String logLevel =
			 * configProps.getProperty("logLevel"); Level level = Level.OFF; if
			 * ("SEVERE".equalsIgnoreCase(logLevel)) { level = Level.SEVERE; } else if
			 * ("WARNING".equalsIgnoreCase(logLevel)) { level = Level.WARNING; } else if
			 * ("INFO".equalsIgnoreCase(logLevel)) { level = Level.INFO; } else if
			 * ("CONFIG".equalsIgnoreCase(logLevel)) { level = Level.CONFIG; } else if
			 * ("FINE".equalsIgnoreCase(logLevel)) { level = Level.FINE; } else if
			 * ("FINER".equalsIgnoreCase(logLevel)) { level = Level.FINER; } else if
			 * ("FINEST".equalsIgnoreCase(logLevel)) { level = Level.FINEST; } else if
			 * ("OFF".equalsIgnoreCase(logLevel)) { level = Level.OFF; }
			 * 
			 * config.setLogConfig(configProps.getProperty("logDirectory"), level); }
			 */

			config.setLogConfig(null, Level.FINE);

			try {
				Platform.startup(config);
			} catch (BirtException e) {
				e.printStackTrace();
			}

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					// IDesignEngineFactory factory = (IDesignEngineFactory) Platform

					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			birtEngine = factory.createReportEngine(config);

		}
		return birtEngine;
	}

	public static synchronized void destroyBirtEngine() {
		if (birtEngine == null) {
			return;
		}
		birtEngine.shutdown();
		Platform.shutdown();
		birtEngine = null;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
