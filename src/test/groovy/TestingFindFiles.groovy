import org.slf4j.Logger
import org.slf4j.LoggerFactory


import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Ivan on 30.07.2017.
 */

class TestingFindFiles extends GroovyTestCase {
    public static final Logger log = LoggerFactory.getLogger(this.class)

    void testMain() {
        log.info("--------------RUNNING TEST: check getFiles() func-----------------------")
        String addr = "D:\\test";
        String fileFormat = ".log";
        List<File> getFilesFuncResult = Main.getFiles(addr, fileFormat)
        List<String> getFilesResultToCheck = new ArrayList<>()
        for (File f : getFilesFuncResult) {
            getFilesResultToCheck.add(f.toString())
        }
        def correctResult = new File(".\\src\\test\\resources\\getFilesCorrectResult.txt").readLines()
        assertEquals(correctResult, getFilesResultToCheck)
        log.info("--------------TEST PASSED-----------------------" + System.lineSeparator())


        log.info("--------------RUNNING TEST: check isFileContains() func-----------------------")
        List<Boolean> isContainsFuncResult = new ArrayList<>();
        List<String> isContainsResultToCheck = new ArrayList<>()
        for (File f : getFilesFuncResult) {
            isContainsFuncResult.add(Main.isFileContains("Passed", f))
        }
        for (Boolean f : isContainsFuncResult) {
            isContainsResultToCheck.add(f.toString())
        }
        def isContainsCorrectResult = new File(".\\src\\test\\resources\\isFileContainsCorrectResult.txt").readLines()
        assertEquals(isContainsCorrectResult, isContainsResultToCheck)
        log.info("--------------TEST PASSED-----------------------")
    }
}