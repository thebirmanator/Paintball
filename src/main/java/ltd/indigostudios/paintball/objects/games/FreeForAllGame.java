package ltd.indigostudios.paintball.objects.games;

import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.utils.Text;

import java.util.List;

public class FreeForAllGame extends PaintballGame {

    private static String[] summary;

    public FreeForAllGame(Arena arena) {
        super(arena);
    }

    protected FreeForAllGame() {

    }

    @Override
    public String[] getGameSummary() {
        if (summary == null) {
            List<String> summaryList = getGameConfig().getStringList("summary.free-for-all");
            summary = new String[summaryList.size()];
            for (String configSummary : summaryList) {
                summary[summaryList.indexOf(configSummary)] = Text.format(configSummary);
            }
        }
        return summary;
    }
}
