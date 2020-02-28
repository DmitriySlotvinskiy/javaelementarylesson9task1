package com.slotvinskiy;

// Есть документ со списком URL:
//https://drive.google.com/open?id=1wVBKKxpTKvWwuCzqY1cVXCQZYCsdCXTl
//Вывести топ 10 доменов которые встречаются чаще всего.
//В документе могут встречатся пустые и недопустимые строки.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String fileName = "./resources/urls.txt";
        Map<String, Integer> map = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {                      //skip empty string
                    continue;
                }
                for (int i = 0; i < line.length(); i++) {   //separate domains from URL and save it in sb
                    if (line.charAt(i) != '/') {
                        if (line.charAt(i) == ' ' && sb.length() == 0) { //delete extra spaces in the begin
                            continue;
                        }
                        sb.append(line.charAt(i));
                    } else {
                        break;
                    }
                }
                deletePrefixMobileOrWWW(sb);
                putLineIn(map, sb.toString());
                sb.delete(0, sb.length());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        List<Info> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Info domainInfo = new Info(entry.getKey(), entry.getValue());
            list.add(domainInfo);
        }

        Collections.sort(list, new Comparator<Info>() {
            @Override
            public int compare(Info o1, Info o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });
        print(list);
    }

    private static void deletePrefixMobileOrWWW(StringBuilder sb) {
        if (sb.length() != 0 && sb.substring(0, 4).equals("www.")) {
            sb.delete(0, 4);
        }
        if (sb.length() != 0 && sb.substring(0, 2).equals("m.")) {
            sb.delete(0, 2);
        }
    }

    private static void putLineIn(Map<String, Integer> map, String domain) {
        if (map.containsKey(domain)) {
            int oldNumberOfDomainLines = map.get(domain);
            map.put(domain, oldNumberOfDomainLines + 1);
        } else {
            map.put(domain, 1);
        }
    }

    private static void print(List<Info> topTen) {
        int place = 1;
        for (Info entry : topTen) {
            System.out.println(place + ") " + entry.getName() + " (" + entry.getValue() + ")");
            place++;
            if (place > 10) {
                break;
            }
        }
    }
}