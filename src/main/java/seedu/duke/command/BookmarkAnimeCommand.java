package seedu.duke.command;

import seedu.duke.anime.Anime;
import seedu.duke.anime.AnimeData;
import seedu.duke.bookmark.Bookmark;
import seedu.duke.exception.AniException;
import seedu.duke.human.UserManagement;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BookmarkAnimeCommand extends Command {

    private int bookmarkIndex;
    private int animeIndex;
    private int bookmarkEpisode;
    // e for edit, a for add, d for delete
    private String bookmarkAction;
    private static final Logger LOGGER = Logger.getLogger(Command.class.getName());

    public BookmarkAnimeCommand(String description) throws AniException {
        // Set log levels
        LOGGER.setLevel(Level.WARNING);
        this.description = description;
        String[] paramGiven = getSplitDescription(description);
        parameterParser(paramGiven[1]);
        setFirstParameter(paramGiven[0]);
        LOGGER.log(Level.INFO,"Successfully loaded fields for Bookmark command.");
    }

    @Override
    public String execute(AnimeData animeData, UserManagement userManagement) throws AniException {
        String result = "";
        Bookmark bookmark = userManagement.getActiveUser().bookmark;

        switch (bookmarkAction) {
        case "e":
            LOGGER.log(Level.INFO,"Executing Edit Episode.");
            result = editBookmarkEpisode(animeData, bookmark);
            break;
        case "a":
            LOGGER.log(Level.INFO,"Executing Add Anime to Bookmark.");
            result = addBookmarkEntry(animeData, bookmark);
            break;
        case "d":
            LOGGER.log(Level.INFO,"Executing Delete Anime from Bookmark.");
            result = deleteBookmarkEntry(animeData, bookmark);
            break;
        case "l":
            LOGGER.log(Level.INFO,"Executing List all anime in Bookmark.");
            result = "Listing all anime in bookmark: ";
            String bookmarkList = listBookmarkEntries(animeData, bookmark);
            result += bookmarkList;
            break;
        default:
            break;
        }
        LOGGER.log(Level.INFO,"Execute Bookmark command successful.");
        return result;
    }

    private String listBookmarkEntries(AnimeData animeData, Bookmark bookmark) {
        String bookmarkList = bookmark.animeListInString(animeData);
        return bookmarkList;
    }

    private String deleteBookmarkEntry(AnimeData animeData, Bookmark bookmark) throws AniException {
        String result;
        if (bookmarkIndex > bookmark.getBookmarkSize() || bookmarkIndex <= 0) {
            String invalidBookmarkIndex = "Bookmark index " + bookmarkIndex + "provided is invalid."
                    + System.lineSeparator() + " Bookmark index is outside Bookmark range (too big or too small).";
            LOGGER.log(Level.WARNING,"Bookmark command execute failed:" + invalidBookmarkIndex);
            throw new AniException(invalidBookmarkIndex);
        }
        Anime animeToDelete = bookmark.getAnimeBookmarkByIndex(animeData, bookmarkIndex - 1);
        result = "Removing " + animeToDelete.getAnimeName() + "! :(";
        bookmark.removeAnimeBookmark(bookmarkIndex - 1);
        return result;
    }


    private String addBookmarkEntry(AnimeData animeData, Bookmark bookmark) throws AniException {
        String result;
        if (animeIndex > animeData.getSize() || animeIndex <= 0) {
            String invalidAnimeIndex = "Anime index " + animeIndex + "provided is invalid."
                    + System.lineSeparator() + " Anime index is outside AnimeData range (too big or too small).";
            LOGGER.log(Level.WARNING,"Bookmark command execute failed:" + invalidAnimeIndex);
            throw new AniException(invalidAnimeIndex);
        }

        Anime animeToAdd = animeData.getAnimeByID(animeIndex - 1);
        result = "Saving " + animeToAdd.getAnimeID() + ". " + animeToAdd.getAnimeName() + " " + " to bookmark.";
        bookmark.addAnimeBookmark(animeToAdd.getAnimeID());
        return result;
    }

    private String editBookmarkEpisode(AnimeData animeData, Bookmark bookmark) throws AniException {

        if (bookmarkIndex > bookmark.getBookmarkSize() || bookmarkIndex <= 0) {
            String invalidBookmarkIndex = "Bookmark index " + bookmarkIndex + "provided is invalid."
                    + System.lineSeparator() + " Bookmark index is outside Bookmark range (too big or too small).";
            LOGGER.log(Level.WARNING,"Bookmark command execute failed:" + invalidBookmarkIndex);
            throw new AniException(invalidBookmarkIndex);
        }
        String result;
        assert bookmarkEpisode >= 0 : "bookmarkEpisode should be positive";
        bookmark.editAnimeBookmarkEpisode(bookmarkIndex - 1, bookmarkEpisode);
        Anime animeToEdit = bookmark.getAnimeBookmarkByIndex(animeData, bookmarkIndex - 1);
        result = "Editing " + animeToEdit.getAnimeName() + " to have " + bookmarkEpisode + " episode";
        return result;
    }


    private void setFirstParameter(String paramGiven) throws AniException {
        //Action edit(e) requires first parameter as bookmarkIndex
        if (bookmarkAction.equals("e")) {
            boolean isValidBookmarkIndex = setBookmarkIndex(paramGiven.trim());
            if (!isValidBookmarkIndex) {
                String invalidBookmarkIndex = "" + paramGiven + " is an invalid parameter!"
                        + System.lineSeparator() + " Bookmark index for edit episode requires integer.";
                LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidBookmarkIndex);
                throw new AniException(invalidBookmarkIndex);
            }
        } else {
            boolean isEmpty = paramGiven.trim().equals("");
            if (!isEmpty) {
                String invalidFirstParameter = "" + paramGiven + " is an invalid parameter!"
                        + System.lineSeparator() + " Add or Delete should not have extra param.";
                LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidFirstParameter);
                throw new AniException(invalidFirstParameter);
            }
        }
    }

    private String[] getSplitDescription(String description) throws AniException {
        String[] paramGiven = description.split("-");
        if (paramGiven.length > 2) {
            String invalidDescription =  description + " has too many parameter!";
            LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidDescription);
            throw new AniException(invalidDescription);
        } else if (paramGiven.length < 2) {
            String invalidDescription =  description + " has too few parameter!";
            LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidDescription);
            throw new AniException(invalidDescription);
        }
        return paramGiven;
    }

    private void parameterParser(String paramGiven) throws AniException {
        String[] paramParts = paramGiven.split(" ");
        switch (paramParts[0].trim()) {
        case "e":
            paramLengthCheck(paramParts);
            setBookmarkAction(paramParts[0]);
            Boolean isValidBookmarkEpisode = setBookmarkEpisode(paramParts[1].trim());
            if (!isValidBookmarkEpisode) {
                String invalidParameter = "-" + paramGiven + " is an invalid parameter!"
                        + System.lineSeparator() + " Bookmark edit episode param requires integer.";
                LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidParameter);
                throw new AniException(invalidParameter);
            }
            break;
        case "a":
            paramLengthCheck(paramParts);
            setBookmarkAction(paramParts[0]);
            Boolean isValidAnimeIndex = setAnimeIndex(paramParts[1].trim());
            if (!isValidAnimeIndex) {
                String invalidParameter = "-" + paramGiven + " is an invalid parameter!"
                        + System.lineSeparator() + " Bookmark Add param requires integer.";
                LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidParameter);
                throw new AniException(invalidParameter);
            }
            break;
        case "d":
            paramLengthCheck(paramParts);
            setBookmarkAction(paramParts[0]);
            Boolean isValidBookmarkIndex = setBookmarkIndex(paramParts[1].trim());
            if (!isValidBookmarkIndex) {
                String invalidParameter = "-" + paramGiven + " is an invalid parameter!"
                        + System.lineSeparator() + " Bookmark delete param requires integer.";
                LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidParameter);
                throw new AniException(invalidParameter);
            }
            break;
        case "l":
            setBookmarkAction(paramParts[0]);
            break;
        default:
            String invalidParameter = "-" + paramGiven + " is an invalid parameter!";
            throw new AniException(invalidParameter);
        }
    }

    private void paramLengthCheck(String[] paramParts) throws AniException {
        // Parameter Additional Field Check
        if (paramParts.length < 2) {
            String invalidParameter = "Parameter : " + paramParts[0] + " requires an additional field";
            LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidParameter);
            throw new AniException(invalidParameter);
        } else if (paramParts.length > 2) {
            String invalidParameter = "Parameter : " + paramParts[0] + " has too much fields";
            LOGGER.log(Level.WARNING,"Could not load bookmark command:" + invalidParameter);
            throw new AniException(invalidParameter);
        }
    }

    private void setBookmarkAction(String actionString) {
        this.bookmarkAction = actionString;
    }

    private boolean setBookmarkIndex(String bookmarkIndexString) {
        if (isInt(bookmarkIndexString)) {
            bookmarkIndex = Integer.parseInt(bookmarkIndexString);
            return true;
        } else {
            return false;
        }
    }

    private boolean setAnimeIndex(String animeIndexString) {
        if (isInt(animeIndexString)) {
            animeIndex = Integer.parseInt(animeIndexString);
            return true;
        } else {
            return false;
        }
    }

    private boolean setBookmarkEpisode(String bookmarkEpisodeString) {
        if (isInt(bookmarkEpisodeString)) {
            bookmarkEpisode = Integer.parseInt(bookmarkEpisodeString);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if String is a parsable int.
     *
     * @param checkStr string to check
     * @return true if parsable int
     */
    public boolean isInt(String checkStr) {
        return checkStr.matches("-?\\d+(\\.\\d+)?");
    }

}
