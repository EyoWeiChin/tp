package seedu.duke.parser;

import seedu.duke.command.SearchCommand;
import seedu.duke.exception.AniException;

public class SearchParser extends CommandParser {
    protected static final String NAME_PARAM = "n";
    protected static final String GENRE_PARAM = "g";
    private SearchCommand searchCommand;

    public SearchParser() {
        searchCommand = new SearchCommand();
    }

    public SearchCommand parse(String description) throws AniException {
        String[] paramGiven = parameterSplitter(description);
        paramIsSetCheck(paramGiven);
        parameterParser(paramGiven);
        return searchCommand;
    }

    public void parameterParser(String[] paramGiven) throws AniException {
        for (String param : paramGiven) {
            String[] paramParts = param.split(" ", 2);
            if (paramParts.length == 0) {
                break;
            }
            switch (paramParts[0].trim()) {
            case "": //skip the first empty param
                break;
            case NAME_PARAM:
                paramFieldCheck(paramParts);
                searchCommand.setSearchTerm(paramParts[1]);
                break;
            case GENRE_PARAM:
                paramFieldCheck(paramParts);
                searchCommand.setSearchGenre(paramParts[1]);
                break;
            default:
                String invalidParameter = PARAMETER_ERROR_HEADER + param + NOT_RECOGNISED;
                throw new AniException(invalidParameter);
            }
        }
    }
}