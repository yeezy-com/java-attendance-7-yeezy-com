package attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
