package core;

import ui.GanttEntity;
import java.util.List;

public class GanttData {
    private String output;
    private List<GanttEntity> ganttEntities;

    public GanttData(String output, List<GanttEntity> ganttEntities) {
        this.output = output;
        this.ganttEntities = ganttEntities;
    }

    public String getOutput() {
        return output;
    }

    public List<GanttEntity> getGanttEntities() {
        return ganttEntities;
    }
}
