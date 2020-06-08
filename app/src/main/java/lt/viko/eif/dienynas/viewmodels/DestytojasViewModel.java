package lt.viko.eif.dienynas.viewmodels;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModel;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.gauriinfotech.commons.Commons;
import lt.viko.eif.dienynas.fragments.MainFragment;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.repositories.StorageRepository;
import lt.viko.eif.dienynas.tasks.SearchTask;
import lt.viko.eif.dienynas.utils.App;
import lt.viko.eif.dienynas.utils.ApplicationData;

public class DestytojasViewModel extends ViewModel {
    private static final String TAG = DestytojasViewModel.class.getSimpleName();

    private DataFormatter dataFormatter = new DataFormatter();
    private StorageRepository repo = StorageRepository.getInstance();
    private List<Destytojas> destList = new ArrayList<>();
    private SearchTask searchTask;


    public void addGroup(Group group) {
        repo.addGroup(group);
    }

    public void setDest(Destytojas dest) {
        repo.setDest(dest);
    }

    public void saveGrades(Group group) {
        Destytojas dest = ApplicationData.getDestytojas();
        dest.getGroup().set((int) group.getId() - 1, group);
        repo.setDest(dest);
    }

    public void addBulkStudentsFromExcel(Uri currentUri) throws IOException, InvalidFormatException {
        List<Student> students = new ArrayList<>();
        List<Integer> grades = new ArrayList<>();
        Destytojas dest = ApplicationData.getDestytojas();
        Group group = dest.getGroup().get((int) ApplicationData.getGroupId());
        for (String task : group.getTask()) {
            grades.add(0);
        }

        String path = Commons.getPath(currentUri, App.getContext());
        Workbook workbook = WorkbookFactory.create(new File(path));
        Sheet sheet = workbook.getSheetAt(0);

        int codeIndex = 0;
        int nameIndex = 0;
        int rowIndex = 0;
        boolean found1 = false;
        boolean found2 = false;

        for (Row row : sheet) {
            for (Cell cell : row) {
//                Log.i(TAG, "addBulkStudentsFromExcel: "+ dataFormatter.formatCellValue(cell) + cell.getColumnIndex());
                if (dataFormatter.formatCellValue(cell).contains("Kodas")) {
                    rowIndex = row.getRowNum();
                    codeIndex = cell.getColumnIndex();
                    found1 = true;
                }
                if (dataFormatter.formatCellValue(cell).contains("Vardas") || dataFormatter.formatCellValue(cell).contains("vardas")) {
                    nameIndex = cell.getColumnIndex();
                    found2 = true;
                }
            }
            if (found1 && found2) break;
        }

        for (int i = rowIndex + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Student stud = new Student();
            stud.setCode(dataFormatter.formatCellValue(row.getCell(codeIndex)));
            stud.setFullName(dataFormatter.formatCellValue(row.getCell(nameIndex)));
            stud.setGrades(grades);
            students.add(stud);
        }
        group.getStudents().addAll(students);
        //Uncomment to update to firebase
        //setDest(dest);

    }

    public void searchForGrades2(String code, MainFragment mainFragment, ProgressBar progressBar) {
        Log.i(TAG, "searchForGrades2: executed");
        searchTask = new SearchTask(code, mainFragment, progressBar);
        Log.i(TAG, "searchForGrades2: " + searchTask.toString());
        searchTask.execute();
    }

//    public void cancelSearchTask() {
//        if (searchTask != null) {
//            searchTask.cancel(true);
//        }
//    }


    public boolean exportGroupToPdf(Group group) {
        boolean completed = false;
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extstoragedir, "Dienynas");
        if (!folder.exists()) {
            boolean bool = folder.mkdir();
        }
        try {
            String fileName = String.format("%s.pdf", group.getName());
            final File file = new File(folder, fileName);
            boolean bool = file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, false);
            Document document = new Document();
            PdfWriter.getInstance(document, fOut);
            document.open();
            PdfPTable table = new PdfPTable(group.getTask().size() + 1);

            addGroupNameToPdf(document, group.getName());

            createTableHeader(table, group.getTask());
            addStudentInfo(table, group.getStudents());

            document.add(table);

            document.close();
            completed = true;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return completed;
    }

    private void createTableHeader(PdfPTable table, List<String> task) {
        PdfPCell headerCell = new PdfPCell();
        headerCell.setGrayFill(0.9f);
        headerCell.setPhrase(new Phrase("Code\nFirst\nLast"));
        //header
        table.addCell(headerCell);
        for (String header : task) {
            headerCell.setPhrase(new Phrase(header));
            table.addCell(headerCell);
        }
        table.completeRow();
    }

    private void addStudentInfo(PdfPTable table, List<Student> students) {
        PdfPCell infoCell = new PdfPCell();
        infoCell.setGrayFill(0.9f);

        PdfPCell gradeCell = new PdfPCell();
        gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradeCell.setVerticalAlignment(Element.ALIGN_CENTER);
        //stud info + grades
        Phrase phrase1;
        for (Student stud : students) {
            phrase1 = new Phrase(stud.getCodeFullName());
            infoCell.setPhrase(phrase1);
            table.addCell(infoCell);
            for (int g : stud.getGrades()) {
                phrase1 = new Phrase(String.valueOf(g));
                gradeCell.setPhrase(phrase1);
                table.addCell(gradeCell);
            }
            table.completeRow();
        }
    }

    private void addGroupNameToPdf(Document document, String name) throws DocumentException {
        Phrase phrase = new Phrase(name);
        PdfPTable table1 = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();

        cell.setFixedHeight(20);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(phrase);
        cell.setBorder(Rectangle.NO_BORDER);

        table1.addCell(cell);

        document.add(table1);

    }
}