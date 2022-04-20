package com.life888888.pdfbox.tools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "ApplyPDFInfo", mixinStandardHelpOptions = true, showAtFileInUsageHelp = true, header = "Set PDF Document properties.%n", footer = "%nUsage Exmaple1:%n"
		+ "pdfbox2 ApplyPDFInfo -t \"My Title\" -s \"My Subject\" -a \"Demo Author1\" -o README.NEW.pdf README.pdf%n"
		+ "%n%nUseage Exmaple2:%n" + "pdfbox2 ApplyPDFInfo @README.props%n%n"
		+ "%n#####README.props%n#####%n--title \"Test Title 1\"%n--keywords \"My Keyword 1, My Keyword 2, My Keyword 3\"%n--author \"Author1\"%n--subject \"Subject 1\"%n--producer \"Producer 1\"%n--creator \"pdfbox-app-ext\"%n--creationDate 2022-05-16T19:08:36.000+08:00%n--modificationDate 2022-05-16T19:02:56.000+08:00%n--outputfile DEMO.pdf%nREADME.pdf%n#####"
		+ "%n%nUseage Exmaple3:%npdfbox2 ApplyPDFInfo @README.pdf.props.txt -o README.NEW.pdf README.pdf"
		+ "%n%n#####README.pdf.props.txt%n#####%n--title \"My Title A\"%n--subject \"My Subject B\"%n--keywords \"My keywords C1, My keywords C2, My keywords C3\"%n--author \"Author D\"%n--creator \"PDFBoxExt\"%n--producer \"DemoApp1\"%n--creationDate \"2022-04-16T19:09:00.000+08:00\"%n--modificationDate \"2022-04-16T19:02:56.000+08:00\"%n#####%n"
		+ "%n%nUseage Exmaple4:%npdfbox2 ApplyPDFInfo @README.pdf.props.txt -U -o README.NEW.pdf README.pdf"
		+ "%n%n#####README.pdf.props.txt%n#####%n--title \"My Title A\"%n--subject \"NULL\"%n--keywords \"My keywords C1, My keywords C2, My keywords C3\"%n--author \"NULL\"%n--creator \"PDFBoxExt\"%n--producer \"DemoApp1\"%n--creationDate \"2022-04-16T19:09:00.000+08:00\"%n--modificationDate \"2022-04-16T19:02:56.000+08:00\"%n#####%n", description = "Prints usage help and version help when requested.%n")
public class ApplyPDFInfo implements Callable<Integer> {

	// 必要參數1個
	@Parameters(arity = "1..1", description = "The PDF document to use")
	List<String> INPUTFILE = null;

	@Option(names = { "--allowSetNULL", "-U" }, description = "Allow Set NULL value to attributes - NULL %nif set this , and value is NULL will set attribute to be NULL!%nif not set this , and value is NULL will skip to set attribute!")
	boolean allowSetNULL = false;
	
	@Option(names = { "--author", "-a" }, description = "Author")
	String author = null;

	@Option(names = { "--title", "-t" }, description = "Title")
	String title = null;

	@Option(names = { "--subject", "-s" }, description = "Subject")
	String subject = null;

	@Option(names = { "--keywords", "-k" }, description = "Keywords")
	String keywords = null;

	@Option(names = { "--creator", "-c" }, description = "Creator")
	String creator = null;

	@Option(names = { "--producer", "-p" }, description = "Producer")
	String producer = null;

	@Option(names = { "--creationDate",
			"-r" }, description = "Creation Date \nISO Datetime formate -\n\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\" \nEX:\"2022-03-16T19:02:56.000+08:00\"")
	String creationDate = null;

	@Option(names = { "--modificationDate",
			"-m" }, description = "Modification Date \nISO Datetime formate -\n\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\" \nEX:\"2022-03-16T19:02:56.000+08:00\"")
	String modificationDate = null;

	@Option(names = { "-o", "--outputfile" }, description = "Write Out PDF document", required = true)
	String outputfile = null;

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	static DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_DATE_TIME;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	public static void main(String[] args) throws IOException {
		// suppress the Dock icon on OS X
		System.setProperty("apple.awt.UIElement", "true");
		System.exit(new CommandLine(new ApplyPDFInfo()).execute(args));
	}// main

	@Override
	public Integer call() throws Exception {
		PDDocument document = null;
		PDDocumentInformation info = null;
		String inputfile = INPUTFILE.get(0);

		document = PDDocument.load(new File(inputfile));
		info = document.getDocumentInformation();
		/*
		  System.out.println("inputfile = " + inputfile);
		  System.out.println("outputfile = " + outputfile);
		  System.out.println("producer = " + producer); System.out.println("creator = "
		  + creator); System.out.println("author = " + author);
		  System.out.println("keywords = " + keywords); System.out.println("subject = "
		  + subject); System.out.println("title = " + title);
		  System.out.println("creationDate = " + creationDate);
		  System.out.println("modificationDate = " + modificationDate);
                 */
		if (author != null) {
			if ("NULL".equals(author)) {
				if (allowSetNULL==true) info.setAuthor(null);
			} else {
			    info.setAuthor(author);
			}
		}
		
		if (title != null) {
			if ("NULL".equals(title)) {
				if (allowSetNULL==true) info.setTitle(null);
			} else {
				info.setTitle(title);
			}
		}
		
		if (subject != null) {
			if ("NULL".equals(subject)) {
				if  (allowSetNULL==true) info.setSubject(null);
			} else {
				info.setSubject(subject);
			}
		}
		
		if (keywords != null) {
			if ("NULL".equals(keywords)) {
				if (allowSetNULL==true) info.setKeywords(null);
			} else {
				info.setKeywords(keywords);
			}
		}
		if (creator != null) {
			if ("NULL".equals(creator)) {
				if (allowSetNULL==true) info.setCreator(null);	
			} else {
				info.setCreator(creator);	
			}
		}
		if (producer != null) {
			if ("NULL".equals(producer)) {
				if (allowSetNULL==true) info.setProducer(null);
			} else {
				info.setProducer(producer);
			}
		}
		if (creationDate != null) {
			if ("NULL".equals(creationDate)) {
				if (allowSetNULL==true) info.setCreationDate(null);
			} else {
				Date cdate1 = null;
				Calendar cdate0 = null;
				cdate1 = sdf.parse(creationDate);
				cdate0 = Calendar.getInstance();
				cdate0.setTime(cdate1);
				info.setCreationDate(cdate0);
			}
		}
		if (modificationDate != null) {
			if ("NULL".equals(modificationDate)) {
				if (allowSetNULL==true) info.setCreationDate(null);
			} else {
				Date mdate1 = null;
				Calendar mdate0 = null;
				mdate1 = sdf.parse(modificationDate);
				mdate0 = Calendar.getInstance();
				mdate0.setTime(mdate1);
				info.setCreationDate(mdate0);
			}	
		}

		document.setDocumentInformation(info);
		File file = new File(outputfile);
		file.createNewFile();
		document.save(file);
		document.close();
		file = null;

		return 0;
	}

}// class
