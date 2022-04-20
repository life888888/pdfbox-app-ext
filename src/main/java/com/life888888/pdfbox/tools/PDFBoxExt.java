package com.life888888.pdfbox.tools;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

@Command(name = "pdfbox2", mixinStandardHelpOptions = true, versionProvider = PDFBoxExt.ManifestVersionProvider.class, subcommands = {
		ApplyPDFInfo.class, DumpPDFInfo.class })
public class PDFBoxExt implements Callable<Integer> {
	@Option(names = { "-V",
			"--version" }, versionHelp = true, description = "Print version info from the pofbox-app-ext-x.x.jar file's /META-INF/MANIFEST.MF and exit")
	boolean versionRequested;

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			int i1 = new CommandLine(new PDFBoxExt()).execute("-V");
			int i2 = new CommandLine(new PDFBoxExt()).execute("-h");
			System.exit(i1+i2);
		}
		System.exit(new CommandLine(new PDFBoxExt()).execute(args));

	}// main

	@Override
	public Integer call() throws Exception {
		boolean ok = true;
		return ok ? 0 : 1; // exit code
	}

	static class ManifestVersionProvider implements IVersionProvider {
		public String[] getVersion() throws Exception {
			Enumeration<URL> resources = CommandLine.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				try {
					Manifest manifest = new Manifest(url.openStream());
					if (isApplicableManifest(manifest)) {
						Attributes attr = manifest.getMainAttributes();

						return new String[] { "PDFBoxExt version: \"" + get(attr, "Implementation-Version") + "\"" };
					}
				} catch (IOException ex) {
					return new String[] { "Unable to read from " + url + ": " + ex };
				}
			}
			return new String[0];
		}

		private boolean isApplicableManifest(Manifest manifest) {
			Attributes attributes = manifest.getMainAttributes();
			return "Apache PDFBox application extension".equals(get(attributes, "Implementation-Title"));
		}

		private static Object get(Attributes attributes, String key) {
			return attributes.get(new Attributes.Name(key));
		}
	}

}// class
