package com.mindhub.homebanking.Services.Implement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.Services.PDFGeneratorService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

@Service
public class PDFGeneratorServiceImplement implements PDFGeneratorService {

    @Override
    public byte[] generateTransactionsPDF(String accountNumber, Set<TransactionDTO> transactions) {
        Font titleFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLD));
        Font sectionFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD));
        Font textFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 14));

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            document.addTitle("Listado de transacciones de cuenta");
            document.addAuthor("Jenifer Roelstraete");
            document.addCreator("Jenifer Roelstraete");

            Chunk title = new Chunk("Listado de transacciones de cuenta" + Chunk.NEWLINE, titleFont);
            document.add(title);

            Phrase accountDetail = new Phrase("Cuenta número: " + accountNumber + Chunk.NEWLINE, sectionFont);
            document.add(accountDetail);

            PdfPCell columnHeader;
            String[] columnTitles = new String[]{"Date", "Description", "Amount", "Balance"};
            PdfPTable transactionsTable = new PdfPTable(columnTitles.length);

            for (String columnTitle : columnTitles) {
                columnHeader = new PdfPCell(new Phrase(columnTitle));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                transactionsTable.addCell(columnHeader);
            }

            transactionsTable.setHeaderRows(1);

            transactions.stream().sorted(Comparator.comparing(transactionDTO -> transactionDTO.getDate())).forEach(transactionDTO -> {
                String strDate = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(transactionDTO.getDate());
                transactionsTable.addCell(strDate);
                transactionsTable.addCell(transactionDTO.getDescription());
                PdfPCell amount = new PdfPCell(Phrase.getInstance(transactionDTO.getAmount().toString()));
                amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell balance = new PdfPCell(Phrase.getInstance(transactionDTO.getCurrentBalance() != null ? transactionDTO.getCurrentBalance().toString() : "0"));
                balance.setHorizontalAlignment(Element.ALIGN_RIGHT);
                transactionsTable.addCell(amount);
                transactionsTable.addCell(balance);
            });

            document.add(transactionsTable);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

}
