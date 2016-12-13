package com.github.cianfree.ip;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.rtree.ip.IPData;
import com.github.cianfree.rtree.ip.IPNode;
import com.github.cianfree.rtree.ip.IPRTree;
import com.github.cianfree.rtree.ip.IPRange;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 纯真IP分析
 *
 * @author Arvin
 * @time 2016/12/9 14:37
 */
public class CunzhenIpAnalyzerTest {

    @Test
    public void testCunzhenIpAnalyze() throws IOException {
        String source = "qqwry-20161209.txt";

        int limit = Integer.MAX_VALUE;
        List<IPModel> ipList = parseIpModels(source, limit);

        IPRTree tree = new IPRTree(4);

        long begTime = System.currentTimeMillis();

        for (int i = 0; i < ipList.size(); ++i) {
            IPNode node = createIPNode(ipList.get(i));
            if (node != null) {
                tree.insert(node);
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("耗时： " + (endTime - begTime) + " 毫秒！");

        System.out.println(tree);

        System.out.println(JSON.toJSON(tree.search("113.106.251.86")));

    }

    private IPNode createIPNode(IPModel ipModel) {
        try {
            IPNode node = new IPNode();
            IPData data = new IPData(ipModel.getAddress(), ipModel.getBegin(), ipModel.getEnd());
            node.setData(data);
            IPRange range = new IPRange(ipToLong(ipModel.getBegin()), ipToLong(ipModel.getEnd()));
            node.setRange(range);
            return node;
        } catch (Exception e) {
            return null;
        }
    }

    private long ipToLong(String ip) {
        try {
            String[] array = ip.split("\\.");
            StringBuilder builder = new StringBuilder("1");
            for (String item : array) {
                if (item.length() == 1) {
                    builder.append("00").append(item);
                } else if (item.length() == 2) {
                    builder.append("0").append(item);
                } else {
                    builder.append(item);
                }
            }
            return Long.parseLong(builder.toString());
        } catch (Exception e) {
            System.out.println("IP: " + ip);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected List<IPModel> parseIpModels(String chunzhenFile, int limit) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(chunzhenFile));

        List<IPModel> ipList = new ArrayList<>();

        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            String[] array = line.split(" +");
            IPModel model = new IPModel(getArrayValue(array, 0, null), getArrayValue(array, 1, null), getArrayValue(array, 2, null), getArrayValue(array, 3, null));
            if (model.getBegin() != null && model.getEnd() != null && model.getAddress() != null) {
                ipList.add(model);
                count++;
            }
            if (count >= limit) {
                break;
            }
        }

        reader.close();
        return ipList;
    }

    private String getArrayValue(String[] array, int index, String def) {
        if (index >= array.length) {
            return def;
        }
        return array[index];
    }
}
