package staffRosteringSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileOperations {
	public <T> List<T> loadData(String filePath, Function<String, T> parser) {
		List<T> items = new ArrayList<>();
		File file = new File(filePath);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					T item = parser.apply(line);
					if (item != null) {
						items.add(item);
					}
				}
			}
		} catch (IOException e) {
            System.out.println("Error loading data : " + e.getMessage());
		}
		return items;
	}
	
	public <T> boolean saveData(String filePath, List<T> items, Function<T, String> formatter) {
		File file = new File(filePath);
		
		// Create parent directories e.g. Data/
		File parentDir = file.getParentFile();
		if (parentDir != null && !parentDir.exists()) 
			parentDir.mkdirs();
		
		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
			for (T item : items) {
				String line = formatter.apply(item);
				if (line != null && !line.isEmpty()) {
					writer.println(line);
				}
			}
			return true;
		} catch (IOException e) {
			System.out.println("Error saving data : " + e.getMessage());
			return false;
		}
	}
	
	public boolean initializeFile(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
	        System.out.println("Error: File path cannot be null or empty");
	        return false;
	    }
		
        File file = new File(filePath);
        
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
            
        }
		
		if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file : " + e.getMessage());
                return false;
            }
        }
		return true;
	}
	
}	