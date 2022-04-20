package com.life888888.pdfbox.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "DumpPDFInfo", mixinStandardHelpOptions = true, showAtFileInUsageHelp = true, header = "Show PDF Document properties.%n", footer = "Usage Exmaple1:%npdfbox2 DumpPDFInfo -e README.pdf%n%nUsage Exmaple2:%npdfbox2 DumpPDFInfo -a -t -s README.pdf%n%nUsage Exmaple3:%npdfbox2 DumpPDFInfo -e -f README.pdf%n%nUsage Exmaple4:%npdfbox2 DumpPDFInfo -e -F README.pdf.props README.pdf%n", description = "Prints usage help and version help when requested.%n")
public class DumpPDFInfo implements Callable<Integer> {
	String outStr = "";

	// 必要參數1個
	@Parameters(arity = "1..1", description = "The PDF document to use")
	List<String> INPUTFILE = null;

	static class PdfInfoSingleArgGroup {
		@Option(names = { "--numofpages", "-n" }, description = "Number Of PDF Pages")
		private static boolean showPages = false;

		@Option(names = { "--title", "-t" }, description = "Title")
		private static boolean showTitle = false;

		@Option(names = { "--author", "-a" }, description = "Author")
		private static boolean showAuthor = false;

		@Option(names = { "--subject", "-s" }, description = "Subject")
		private static boolean showSubject = false;

		@Option(names = { "--keywords", "-k" }, description = "Keywords")
		private static boolean showKeywords = false;

		@Option(names = { "--creator", "-c" }, description = "Creator")
		private static boolean showCreator = false;

		@Option(names = { "--producer", "-p" }, description = "Producer")
		private static boolean showProducer = false;

		@Option(names = { "--creationDate", "-r" }, description = "Creation Date")
		private static boolean showCreationDate = false;

		@Option(names = { "--modificationDate", "-m" }, description = "Modification Date")
		private static boolean showModificationDate = false;
	}

	@ArgGroup(exclusive = false, multiplicity = "0..1", heading = "\nPDF Info Options:\n")
	SaveInfoArgGroup saveInfoArgGroup;

	@ArgGroup(exclusive = true, multiplicity = "1", heading = "\nPDF Info Options:\n")
	PdfInfoArgGroup pdfInfoArgGroup;

	static class SaveInfoArgGroup {
		@ArgGroup(exclusive = true, multiplicity = "1", heading = "\nSave Output To props File:\n")
		SaveFileArgGroup saveFileArgGroup;
	}

	static class PdfInfoArgGroup {
		@ArgGroup(exclusive = false, multiplicity = "1", heading = "\nPDF Info Options one bye one:\n")
		PdfInfoSingleArgGroup pdfInfoSingleArgGroup;

		@Option(names = { "--everything",
				"-e" }, description = "PDF Info Options for all - auto include -n, -t, -a, -s, -k, -c, -p, -r, -m")
		boolean showAll = false;
	}

	static class SaveFileArgGroup {
		@Option(names = { "--saveToPropsFile", "-F" }, description = "Save Output To props File")
		private static String saveToPropsFile = null;

		@Option(names = { "--saveDFile",
				"-f" }, description = "Save Output To Default props File - $inputfile.props.txt")
		private static boolean isSaveToDefaultProsFile = false;
	}

	String saveOutputFile = null;

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	static DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_DATE_TIME;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	public static void main(String[] args) throws IOException, ParseException {
		// suppress the Dock icon on OS X
		System.setProperty("apple.awt.UIElement", "true");
		System.exit(new CommandLine(new DumpPDFInfo()).execute(args));

	}// main

	@SuppressWarnings("static-access")
	@Override
	public Integer call() throws Exception {
		outStr = "";
		boolean isShowAll = false;
		String inputfile = null;
		PDDocument document = null;
		PDDocumentInformation info = null;

		inputfile = INPUTFILE.get(0);
		// System.out.println("inputfile = " + inputfile);
		if (saveInfoArgGroup != null) {
			if (saveInfoArgGroup.saveFileArgGroup.saveToPropsFile != null) {
				saveOutputFile = saveInfoArgGroup.saveFileArgGroup.saveToPropsFile;
				// System.out.println("saveOutputFile ="+saveOutputFile );
			}
			if (saveInfoArgGroup.saveFileArgGroup.isSaveToDefaultProsFile == true) {
				saveOutputFile = inputfile + ".props.txt";
				// System.out.println("saveOutputFile ="+saveOutputFile );
			}
		} // if (saveInfoArgGroup!=null) {

		document = PDDocument.load(new File(inputfile));
		info = document.getDocumentInformation();

		isShowAll = pdfInfoArgGroup.showAll;
		// pages
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showPages || isShowAll) {
			int pages = document.getNumberOfPages();
			dumpP("pages", Integer.toString(pages));
		}
		// title
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showTitle || isShowAll) {
			dumpP("title", info.getTitle());
		}
		// subject
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showSubject || isShowAll) {
			dumpP("subject", info.getSubject());
		}
		// keywords
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showKeywords || isShowAll) {
			dumpP("keywords", info.getKeywords());
		}
		// author
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showAuthor || isShowAll) {
			dumpP("author", info.getAuthor());
		}
		// creator
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showCreator || isShowAll) {
			dumpP("creator", info.getCreator());
		}
		// producer
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showProducer || isShowAll) {
			dumpP("producer", info.getProducer());
		}
		// creationDate
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showCreationDate || isShowAll) {
			String cdate = null;
			if (info.getCreationDate()!=null) cdate = sdf.format(info.getCreationDate().getTime());
			dumpP("creationDate", cdate);
		}
		// modificationDate
		if (pdfInfoArgGroup.pdfInfoSingleArgGroup.showModificationDate || isShowAll) {
			String mdate = null;
			if (info.getModificationDate()!=null) mdate=sdf.format(info.getModificationDate().getTime());
			dumpP("modificationDate", mdate);
		}
		if (saveOutputFile != null) {
			Files.writeString(Paths.get(saveOutputFile), outStr);
		}
		return 0;
	}

	void dumpP(String key, String val) {
		if (val == null) val = "NULL";
		if (saveOutputFile == null) {
			System.out.println(key + "=" + val);
		} else if (saveOutputFile != null) {
			outStr = outStr + "--" + key + " \"" + val + "\"\n";
		}
	}// dumpP

}// class
