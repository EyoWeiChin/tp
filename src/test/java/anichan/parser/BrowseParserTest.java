package anichan.parser;

import anichan.commands.BrowseCommand;
import anichan.exception.AniException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BrowseParserTest {
    private static final String NON_INT_PAGE_NUM = "-p twenty";
    private static final String INVALID_ORDER_TEST = "-o whateverOrder";
    private static final String INVALID_PARAMETERS_TEST1 = "-n name";
    private static final String INVALID_PARAMETERS_TEST2 = "-sort name";
    private static final String INVALID_FIELD_TEST1 = "-s   ";
    private static final String INVALID_FIELD_TEST2 = "-s beep";
    private static final String INVALID_FIELD_TEST3 = "-s -o -p";
    private static final String INVALID_FIELD_TEST4 = "- ";
    private static final String DIFF_ORDER_TEST = "-p 1 -s rating -o asc";
    private static final String DIFF_ORDER_TEST2 = "-s rating -o asc -p 1";
    private static final String INT_OVERFLOW_FIELD_TEST = "-p 999999999999999";

    @Test
    void parse_invalidDashParameters_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("- ");
        });
    }

    @Test
    void parse_invalidDashParameters2_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("---");
        });
    }

    @Test
    void parse_multipleDuplicateSort_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("-s rating -s name");
        });
    }

    @Test
    void parse_paramStacked_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("-s rating-o dsc-p 23");
        });
    }

    @Test
    void parse_multipleDuplicateOrder_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("-o asc -o dsc");
        });
    }

    @Test
    void parse_multipleDuplicatePage_throwAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse("-p 3 -p 5");
        });
    }

    @Test
    void parse_invalidParameter_throwsAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(INVALID_PARAMETERS_TEST1);
        });

        assertThrows(AniException.class, () -> {
            testParse.parse(INVALID_PARAMETERS_TEST2);
        });
    }

    @Test
    void parse_edgeCaseDashBlank_defaultExecution() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(INVALID_FIELD_TEST4);
        });
    }

    @Test
    void parse_invalidPageNum_throwsAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(NON_INT_PAGE_NUM);
        });
    }

    @Test
    void parse_invalidOrderType_throwsAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(INVALID_ORDER_TEST);
        });
    }

    @Test
    void parse_invalidField_throwsAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(INVALID_FIELD_TEST1);
        });

        BrowseParser testParse2 = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse2.parse(INVALID_FIELD_TEST2);
        });

        BrowseParser testParse3 = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse3.parse(INVALID_FIELD_TEST3);
        });
    }

    @Test
    void parse_differentParameterOrder_identicalBrowseSettings() throws AniException {
        BrowseParser testParse = new BrowseParser();
        BrowseParser testParse2 = new BrowseParser();
        BrowseCommand testBrowse = testParse.parse(DIFF_ORDER_TEST);
        BrowseCommand testBrowse2 = testParse2.parse(DIFF_ORDER_TEST2);

        assertEquals(testBrowse.getPage(), testBrowse2.getPage());
        assertEquals(testBrowse.getSortType(), testBrowse2.getSortType());
        assertEquals(testBrowse.getOrder(), testBrowse2.getOrder());
    }

    @Test
    void parse_setPageToIntOverflow_throwsAniException() {
        BrowseParser testParse = new BrowseParser();
        assertThrows(AniException.class, () -> {
            testParse.parse(INT_OVERFLOW_FIELD_TEST);
        });
    }

}
