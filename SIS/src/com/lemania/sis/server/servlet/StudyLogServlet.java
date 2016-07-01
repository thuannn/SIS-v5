package com.lemania.sis.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.server.bean.studylog.StudyLog;
import com.lemania.sis.server.bean.studylog.StudyLogDao;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Label;

@MultipartConfig
@SuppressWarnings("serial")
public class StudyLogServlet extends HttpServlet {

	/*
	 * */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws IOException, ServletException {
		// Get the parameters
		String[] splits = req.getRequestURI().split("/", 0);
		
		if (!splits[0].equals("") || !splits[1].equals("studylog")) {
			throw new IllegalArgumentException(
					"The URL is not formed as expected. "
							+ "Expecting /gcs/<bucket>/<object>");
		}
		StudyLogDao dao = new StudyLogDao();
		List<StudyLog> logs = new ArrayList<StudyLog>();
		logs.addAll( dao.loadBatch( splits[2], splits[3], splits[4].replace("%7C", "|") ) );
		//
		OutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment; filename=sampleName.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
			WritableSheet s = w.createSheet("Demo", 0);
			//
			int row = 0;
			for ( StudyLog log : logs ) {
				s.addCell( new Label(0, row, FieldValidation.swissDateFormat(log.getLogDate())));
				s.addCell( new Label(1, row, log.getClasseName()) );
				s.addCell( new Label(2, row, log.getLogContent()) );
				s.addCell( new Label(3, row, log.getFileName()) );
				row++;
			}
			w.write();
			w.close();
		} catch (Exception e) {
			throw new ServletException("Exception in Excel Sample Servlet", e);
		} finally {
			if (out != null)
				out.close();
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		//
	}
}
