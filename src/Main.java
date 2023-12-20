package AutoHotkey.FolderProcessing;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String directory = promptForDirectory(); //Ask user for directory to be processed
            listFilenamesAndFilePaths(directory); //Lists all filenames and filepaths into .txt files to be processed by AHK
            String programPath = "Path to desired program"; //Program path removed (NDA)
            String filepathsLocation = System.getProperty("user.home") + "\\Documents\\filepaths.txt"; //Temporary files are stored in documents
            String filenamesLocation = System.getProperty("user.home") + "\\Documents\\filenames.txt";

            // Check if filenames.txt and filepaths.txt exist and create new ones
            File filenamesFile = new File(filenamesLocation);
            File filepathsFile = new File(filepathsLocation);
            
            //Read filepaths and names for iteration
            List<String> filepaths = readFilepaths(filepathsLocation);
            List<String> filenames = readFilenames(filenamesLocation);

            //If folder has already processed files, remove them to avoid any conflicts.
            deleteMatchingFiles(directory, filenames);
            
            //Iterator for filepaths
            Iterator<String> iterator = filepaths.iterator();
            while (iterator.hasNext()) {
                String filePath = iterator.next();
                //Opens first file in desired program
                openFileInProgram(filePath, programPath);
                try {
                    // Process the current file with AHK
                    processFile();
                } catch (Exception e) {
                    System.out.println("An error occurred while processing the file: " + filePath);
                    e.printStackTrace();
                }
                // Remove the selected file path from the list using the iterator's remove() method
                iterator.remove();
                // Update filepaths.txt with the remaining file paths
                updateFilepaths(filepathsLocation, filepaths);
                if (filepaths.isEmpty()) {
                    break; // Exit the while loop if there are no more files to process
                }
            }

            // New method call to show the processed files to user on completion
            showProcessedFilesDialog(filenames);

            // Terminate the program
            System.exit(0);
        });
    }

    private static String promptForDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        //Choose only directories
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        //Select directory with GUI
        int result = fileChooser.showDialog(null, "Select Directory");
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("No directory selected. Exiting...");
            System.exit(0);
            return null; // Unreachable, but required for compilation
        }
    }

    private static void processFile() throws Exception {
        //This method handles opening of file in the desired program, and completion of AHK script.
        String programPath = System.getProperty("user.home") + "\\Documents\\AutomaticFileProcessing.exe"; //.exe can be anywhere, documents is just easy location
        ProcessBuilder processBuilder = new ProcessBuilder(programPath);
        Process automaticFileProcessing = processBuilder.start();
        automaticFileProcessing.waitFor();
    }

    private static void listFilenamesAndFilePaths(String directory) {
        File dir = new File(directory);
        //List only specific files ending with .example
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".example")); //File extension removed (NDA)

        if (files != null) {
            try {
                // Create a FileWriter for the filenames text file in the Documents folder
                FileWriter filenamesWriter = new FileWriter(System.getProperty("user.home") + "\\Documents\\filenames.txt");

                // Create a FileWriter for the filepaths text file in the Documents folder
                FileWriter filepathsWriter = new FileWriter(System.getProperty("user.home") + "\\Documents\\filepaths.txt");

                for (File file : files) { // Start from index 1 to skip the first file
                    // Show a prompt for each file, allowing the user to choose whether to ignore it or not (Sometimes only specific files need to be processed)
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to process the file: " + file.getName(), "Process File?", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Write the filename without the .example extension to the filenames text file
                        String filename = file.getName();
                        String filenameWithoutExtension = removeFileExtension(filename);
                        filenamesWriter.write(filenameWithoutExtension + "\n");

                        // Write the filepath to the filepaths text file 
                        filepathsWriter.write(file.getAbsolutePath() + "\n");
                    }
                }

                // Close the file writers
                filenamesWriter.close();
                filepathsWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the text files.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No files found in the directory or only one file is present.");
        }
    }

    private static String removeFileExtension(String filename) {
        //Copies filename from first letter to last letter before .example
        if (filename.endsWith(".example")) {
            int extensionIndex = filename.lastIndexOf(".example");
            return filename.substring(0, extensionIndex);
        }
        return filename;
    }

    public static void openFileInProgram(String filePath, String programPath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        try {
            //Uses processbuilder to handle the file processing and waiting
            ProcessBuilder processBuilder = new ProcessBuilder(programPath, file.getAbsolutePath());
            processBuilder.start();
        } catch (IOException e) {
            System.out.println("An error occurred while opening the file.");
            e.printStackTrace();
        }
    }

    private static List<String> readFilepaths(String filePath) {
        List<String> filepaths = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            //Iterate through all filepaths and add them to arraylist
            while ((line = reader.readLine()) != null) { 
                filepaths.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading " + filePath);
            e.printStackTrace();
        }

        return filepaths;
    }

    private static void updateFilepaths(String filePath, List<String> filepaths) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String path : filepaths) {
                writer.write(path + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating " + filePath);
            e.printStackTrace();
        }
    }

    private static void deleteMatchingFiles(String directory, List<String> filenames) {
        File dir = new File(directory);
        for (String filename : filenames) {
            // Add the file extensions to search for
            String[] extensions = {".example2", ".example3", ".example4"}; //Removed (NDA)
            for (String extension : extensions) {
                //Scans the folder for all files, checks if they match filenames user has selected to be processed, and then checks if they have extensions that match and then add matching files to "files"
                File[] files = dir.listFiles((dir1, name) -> name.contains(filename) && name.toLowerCase().endsWith(extension.toLowerCase()));
                if (files != null) {
                    //Delete all files in "files"
                    for (File file : files) {
                        try {
                            // Delete the file
                            Files.delete(file.toPath());
                            //Notification for user that files have been deleted
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } catch (IOException e) {
                            System.out.println("An error occurred while deleting the file: " + file.getAbsolutePath());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static List<String> readFilenames(String filePath) {
        List<String> filenames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            //Adds all filenames to "filenames"
            while ((line = reader.readLine()) != null) {
                filenames.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading " + filePath);
            e.printStackTrace();
        }

        return filenames;
    }
    private static void showProcessedFilesDialog(List<String> processedFiles) {
        StringBuilder sb = new StringBuilder("Processed Files:\n\n");
        //Lists all files that have been processed
        for (String file : processedFiles) {
            sb.append(file).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        //Notifies the user whch files have been processed
        JOptionPane.showMessageDialog(null, scrollPane, "Processed Files", JOptionPane.INFORMATION_MESSAGE);
    }
}
