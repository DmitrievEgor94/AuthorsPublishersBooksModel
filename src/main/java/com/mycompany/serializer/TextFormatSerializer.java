package com.mycompany.serializer;

import com.mycompany.books_authors_publishers.enteties_for_serializing.Date;
import com.mycompany.books_authors_publishers.enteties_for_serializing.ListField;
import com.mycompany.books_authors_publishers.enteties_for_serializing.OnlyInteger;
import com.mycompany.books_authors_publishers.enteties_for_serializing.PermissionToBeNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TextFormatSerializer implements Serializer {

    private static final String OPEN_BRACKET_FOR_LIST = "[";
    private static final String CLOSE_BRACKET_FOR_LIST = "]";
    private static final String OPEN_BRACKET_FOR_CLASS = "{";
    private static final String CLOSE_BRACKET_FOR_CLASS = "}";
    private static final int SPACES_NUMBER_BETWEEN_ELEMENTS = 2;

    private boolean isFileValid(String content, Class ob) throws FileNotFoundException {
        return areBracketsValid(content) && areObjectsValidInFile(content, ob);
    }

    private boolean areBracketsValid(String content) {
        List<Character> openBrackets = new ArrayList<>();
        List<Character> closeBrackets = new ArrayList<>();

        openBrackets.add(OPEN_BRACKET_FOR_LIST.charAt(0));
        openBrackets.add(OPEN_BRACKET_FOR_CLASS.charAt(0));

        closeBrackets.add(CLOSE_BRACKET_FOR_LIST.charAt(0));
        closeBrackets.add(CLOSE_BRACKET_FOR_CLASS.charAt(0));

        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < content.length(); i++) {
            char currentCharacter = content.charAt(i);

            if (openBrackets.contains(currentCharacter))
                stack.push(currentCharacter);

            if (closeBrackets.contains(currentCharacter)) {
                char openBracket = stack.pop();
                if (openBracket != openBrackets.get(closeBrackets.indexOf(currentCharacter))) return false;
            }
        }

        return stack.empty();
    }

    private boolean areObjectsValidInFile(String content, Class obClass) {
        List<Integer> openBracketClassPositions = getBracketPositions(content, OPEN_BRACKET_FOR_CLASS);
        List<Integer> closeBracketClassPositions = getBracketPositions(content, CLOSE_BRACKET_FOR_CLASS);

        for (int i = 0; i < openBracketClassPositions.size(); i++) {
            int openPositionBracket = openBracketClassPositions.get(i);
            int closePositionBracket = closeBracketClassPositions.get(i);

            String contentOfClass = content.substring(openPositionBracket + 1, closePositionBracket);

            if (!isObjectValidInFile(contentOfClass, obClass)) {
                return false;
            }
        }
        return true;
    }

    private boolean isObjectValidInFile(String content, Class obClass) {
        Field[] declaredFields = obClass.getDeclaredFields();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        AccessibleObject.setAccessible(declaredFields, true);
        StringTokenizer stringTokenizer = new StringTokenizer(content);

        final String ABSENT_DEATH_DATE = "-";

        for (Field declaredField : declaredFields) {
            if (Modifier.isStatic(declaredField.getModifiers())) continue;

            String value;

            if (!stringTokenizer.hasMoreTokens()) {
                return false;
            } else stringTokenizer.nextToken();

            if (!stringTokenizer.hasMoreTokens()) {
                return false;
            } else value = stringTokenizer.nextToken();

            if (declaredField.getDeclaredAnnotation(OnlyInteger.class) != null) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            if (declaredField.getDeclaredAnnotation(PermissionToBeNull.class) != null) {
                if (value.equals(ABSENT_DEATH_DATE)) {
                    continue;
                }
            }

            if (declaredField.getDeclaredAnnotation(ListField.class) != null) {
                if (value.contains(OPEN_BRACKET_FOR_LIST)) {
                    continue;
                } else return false;
            }

            if (declaredField.getDeclaredAnnotation(Date.class) != null) {
                try {
                    LocalDate.parse(value, formatter);
                } catch (DateTimeParseException e) {
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public Object deserializeObject(String fileWithObjects, Class obClass) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        try (Scanner scanner = new Scanner(new File(fileWithObjects))) {
            String content = scanner.useDelimiter("\\Z").next();

            if (!isFileValid(content, obClass)) throw new ClassNotFoundException();

            int posOpenBracketList = content.indexOf(OPEN_BRACKET_FOR_LIST);
            int posCloseBracketList = content.lastIndexOf(CLOSE_BRACKET_FOR_LIST);

            String contentBetweenBrackets = content.substring(posOpenBracketList + 1, posCloseBracketList);

            return getList(contentBetweenBrackets, obClass);
        }
    }

    private List getList(String content, Class obClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List list = new ArrayList();

        List<Integer> openBracketClassPositions = getBracketPositions(content, OPEN_BRACKET_FOR_CLASS);
        List<Integer> closeBracketClassPositions = getBracketPositions(content, CLOSE_BRACKET_FOR_CLASS);

        if (openBracketClassPositions.size() == 0) {

            Scanner scanner = new Scanner(content);

            while (scanner.hasNextInt()) {
                list.add(scanner.nextInt());
            }

        } else {
            for (int i = 0; i < openBracketClassPositions.size(); i++) {
                int openPositionBracket = openBracketClassPositions.get(i);
                int closePositionBracket = closeBracketClassPositions.get(i);

                String contentOfClass = content.substring(openPositionBracket + 1, closePositionBracket);

                list.add(getObject(contentOfClass, obClass));
            }
        }
        return list;
    }

    private List<Integer> getBracketPositions(String content, String bracket) {
        List<Integer> openBracketPositions = new ArrayList<>();

        for (int index = content.indexOf(bracket); index >= 0;
             index = content.indexOf(bracket, index + 1)) {
            openBracketPositions.add(index);
        }

        return openBracketPositions;
    }

    private Object getObject(String content, Class obClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] declaredFields = obClass.getDeclaredFields();

        AccessibleObject.setAccessible(declaredFields, true);
        Constructor constructorWithNoParameters;

        try {
            constructorWithNoParameters = obClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("In class " + obClass + " no constructor without parameters!");
        }

        Object createdObject = constructorWithNoParameters.newInstance();
        Scanner scanner = new Scanner(content);

        for (Field declaredField : declaredFields) {

            if (!Modifier.isStatic(declaredField.getModifiers())) {

                if (declaredField.getType() == List.class) {
                    int posOpenBracket = content.indexOf(OPEN_BRACKET_FOR_LIST);
                    int posCloseBracket = content.lastIndexOf(CLOSE_BRACKET_FOR_LIST);
                    String contentWithObjects = content.substring(posOpenBracket + 1, posCloseBracket);
                    declaredField.set(createdObject, getList(contentWithObjects, Integer.class));
                } else {
                    scanner.next();
                    String value = scanner.next();
                    Object objectForField = declaredField.getType().getConstructor(String.class).newInstance(value);
                    declaredField.set(createdObject, objectForField);
                }
            }
        }

        return createdObject;
    }

    @Override
    public void serializeObject(Object ob, String fileWithObjects) throws FileNotFoundException, IllegalAccessException {
        PrintWriter printWriter = new PrintWriter(new File(fileWithObjects));

        writeObject(ob, 0, printWriter);
        printWriter.flush();
        printWriter.close();
    }

    private void writeObject(Object ob, int numberOfSpaces, PrintWriter fileWithObjects) throws IllegalAccessException {
        Class obClass = ob.getClass();

        if (ob instanceof List) {
            writeInFile(OPEN_BRACKET_FOR_LIST, 0, fileWithObjects);

            List collection = (List) ob;
            boolean primitiveType = false;

            for (Object o : collection) {

                if (canBeTyped(o.getClass())) {
                    primitiveType = true;
                    writeInFile(o.toString(), SPACES_NUMBER_BETWEEN_ELEMENTS, fileWithObjects);
                } else {
                    numberOfSpaces += SPACES_NUMBER_BETWEEN_ELEMENTS;
                    writeObject(o, numberOfSpaces, fileWithObjects);
                    numberOfSpaces -= SPACES_NUMBER_BETWEEN_ELEMENTS;
                }
            }

            if (primitiveType) {
                writeInFile("]\n", SPACES_NUMBER_BETWEEN_ELEMENTS, fileWithObjects);
            } else {
                writeInFile("]\n", numberOfSpaces, fileWithObjects);
            }

        } else {
            while (obClass != Object.class) {
                writeInFile("{\n", numberOfSpaces, fileWithObjects);

                numberOfSpaces += SPACES_NUMBER_BETWEEN_ELEMENTS;

                printFieldsInFile(ob, fileWithObjects, numberOfSpaces);

                if (obClass.getSuperclass() == Object.class) {
                    numberOfSpaces -= SPACES_NUMBER_BETWEEN_ELEMENTS;
                    writeInFile("}\n", numberOfSpaces, fileWithObjects);
                }
                obClass = obClass.getSuperclass();
            }
        }
    }

    private void writeInFile(String information, int numberOfSpaces, PrintWriter fileWithObjects) {
        char[] spaces = new char[numberOfSpaces];
        Arrays.fill(spaces, ' ');
        fileWithObjects.print(spaces);
        fileWithObjects.print(information);
    }

    private boolean canBeTyped(Class obClass) {
        return (obClass == Integer.class || obClass == String.class || obClass.isPrimitive());
    }

    private void printFieldsInFile(Object ob, PrintWriter fileWithObjects, int numberOfSpaces) throws IllegalAccessException {
        Class obClass = ob.getClass();

        Field[] declaredFields = obClass.getDeclaredFields();

        AccessibleObject.setAccessible(declaredFields, true);

        for (Field field : declaredFields) {
            Class fieldClass = field.getType();

            if (!Modifier.isStatic(field.getModifiers())) {
                if (canBeTyped(fieldClass)) {
                    writeInFile(field.getName() + ": " + field.get(ob) + "\n", numberOfSpaces, fileWithObjects);
                } else {
                    writeInFile(field.getName() + ": ", numberOfSpaces, fileWithObjects);
                    writeObject(field.get(ob), numberOfSpaces, fileWithObjects);
                }
            }
        }
    }
}
