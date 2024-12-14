package attendance.domain;

import java.util.List;
import java.util.stream.Collectors;

public class FileConverter {

    public List<String> getNames(List<String> texts) {
        return texts.stream()
                .skip(1)
                .map(text -> text.split(",")[0])
                .collect(Collectors.toSet())
                .stream().toList();

    }
}
