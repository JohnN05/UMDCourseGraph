package courses;

import courses.relationship.requisites.Requisite;
import courses.relationship.requisites.RequisiteFactory;
import utility.CourseValidator;

import java.util.*;

/**
 * Factory class for Course.  Used in CourseList.
 *
 * @author JohnN05
 */

public class CourseFactory {
    //Keywords used to interpret course requisites
    private static final String[] CONJUNCTIONS = {"OR", "AND"};
    private static final HashMap<String, String> KEYWORDS = new HashMap<>();
    static{
        KEYWORDS.put("EARNED", "EXAM"); //for prerequisite exams
        KEYWORDS.put("HIGH","COURSE");  //for high school prerequisite
    }

    private static final String[] RELATIONSHIP_KEYS = {"coreqs", "prereqs", "restrictions", "additional_info"};


    /**
     * Processes a RawCourse into a Course with usable information
     *
     * @param course RawCourse being processed
     * @return Course with formatted information including requisites
     */
    public static Course processCourse(RawCourse course){
        String name = course.getName();
        String course_id = course.getCourse_id();
        int credits = course.getCredits();
        String description = course.getDescription();
        Set<Set<String>> gen_eds = processGenEds(course);
        HashSet<HashSet<Requisite>> prereqs = processPrereqs(course);
        HashSet<HashSet<Requisite>> coreqs = processCoreqs(course);
        return new Course(course_id, name, credits, description, gen_eds, prereqs, coreqs);

    }

    /**
     * Processes and interprets the List of Gen-Eds in a RawCourse
     *
     * @param course the courses.RawCourse containing the Gen Eds that will be processed
     * @return the formatted set of String set Gen Eds.
     * Every set in gen_eds represents a single Gen-Ed credit that can be earned.
     * If there is more than a single String in a Set, the student may decide which Gen-Ed they would like
     * to earn for the course.
     */
    private static Set<Set<String>> processGenEds(RawCourse course){
        Set<Set<String>> gen_eds = new HashSet<>();
        ArrayList<ArrayList<String>> rawGenEds = course.getGen_ed();
        HashSet<String> singleCredit;

        //Handles courses that can provide different gen eds as one credit
        if(rawGenEds.size() > 1){
            singleCredit = new HashSet<>();
            singleCredit.add(rawGenEds.remove(0).get(0));
            singleCredit.add(rawGenEds.get(0).remove(0));

            gen_eds.add(singleCredit);
        }

        if(!rawGenEds.isEmpty()){
            for(String genEd: rawGenEds.get(0)){
                singleCredit = new HashSet<>();
                singleCredit.add(genEd);

                gen_eds.add(singleCredit);
            }
        }

        return gen_eds;
    }

    /**
     * Processes and interprets the Corerequisites stored in the relationships map.
     *
     * @param course rawCourse containing the corerequisites that will be processed
     * @return formatted set of Requisites
     */
    private static HashSet<HashSet<Requisite>> processCoreqs(RawCourse course){
        HashSet<HashSet<Requisite>> coreqs = new HashSet<>();
        Map<String, String> curRelations = course.getRelationships();

        if (curRelations.containsKey(RELATIONSHIP_KEYS[0]) && curRelations.get(RELATIONSHIP_KEYS[0]) != null) {
            StringBuilder rawCoreqs = new StringBuilder(curRelations.get(RELATIONSHIP_KEYS[0]));
            List<String> corereqWords = getReqWords(rawCoreqs);
            filterReqWords(corereqWords);
            coreqs = interpretReqWords(corereqWords);
        }

        return coreqs;
    }

    /**
     * Processes and interprets the prerequisites stored in the relationships map.  Formats the prerequisites
     * into a set of Requisite sets.
     *
     * @param course the RawCourse containing the prerequisites that will be processed
     * @return the formatted set of Requisites.
     * Every set in the set represents a single prerequisite that must be fulfilled prior to taking the course.
     * Requisite sets with size greater than 1 means that the prerequisite can be fulfilled through different ways.
     */
    private static HashSet<HashSet<Requisite>> processPrereqs(RawCourse course) {
        HashSet<HashSet<Requisite>> prereqs;
        Map<String, String> curRelations = course.getRelationships();

        StringBuilder rawPrereqs = getRawPrereqs(curRelations);
        List<String> prereqWords = getReqWords(rawPrereqs);
        filterReqWords(prereqWords);
        prereqs = interpretReqWords(prereqWords);

        return prereqs;
    }

    /**
     * Creates a StringBuilder representing a Course's prerequisites
     * @param relationships Map containing the prerequisite data
     * @return Stringbuilder with corresponding prerequisite data
     */
    private static StringBuilder getRawPrereqs(Map<String, String> relationships){
        StringBuilder rawPrereqs = new StringBuilder();

        if(relationships.containsKey(RELATIONSHIP_KEYS[1]) && relationships.get(RELATIONSHIP_KEYS[1]) != null){
            rawPrereqs.append(relationships.get(RELATIONSHIP_KEYS[1]));
        }
        String morePrereqs = relationships.get(RELATIONSHIP_KEYS[3]);

        //Addresses the API moving prereq descriptions longer than one sentence to "Additional Information:"
        if (morePrereqs != null && morePrereqs.contains(" ")) {
            String firstWord = morePrereqs.substring(0, morePrereqs.indexOf(" ")).toUpperCase();

            //Identifies displaced prerequisite info with the use of specific conjunctions
            for (String s : CONJUNCTIONS) {
                if (s.matches(firstWord)) {
                    rawPrereqs.append(" ").append(morePrereqs, 0, morePrereqs.indexOf("."));
                }
            }
        }

        return rawPrereqs;
    }

    /**
     * Converts the requisite sentence provided by the API into separate words.
     *
     * @param rawReqs the Map of relationship Strings containing the prerequisites
     * @return a List of Strings which represents the words in the prerequisite description.
     * Certain words are recombined if they contain KEYWORDS for future filtering
     */
    private static List<String> getReqWords(StringBuilder rawReqs){

        //separates department abbreviations from names for simple processing and removes punctuation
        rawReqs = new StringBuilder(rawReqs.toString().replaceAll("-", " "));
        rawReqs = new StringBuilder(rawReqs.toString().replaceAll("\\p{Punct}", "").toUpperCase());
        LinkedList<String> rawReqWords = new LinkedList<>(Arrays.asList(rawReqs.toString().split(" ")));
        LinkedList<String> reqWords = new LinkedList<>();

        //recombines certain prerequisite words
        while (!rawReqWords.isEmpty()) {
            boolean combinedWords = false;
            String curWord = rawReqWords.getFirst();
            StringBuilder merged = new StringBuilder();


            //recombines words that mention department permission.  Example: "Permission of ENGR"
            if (curWord.contains("PERMISSION")) {
                combinedWords = true;

                for (int word = 0; word < 3; word++) {
                    if(!rawReqWords.isEmpty()) {
                        merged.append(rawReqWords.removeFirst()).append(" ");
                    }else{
                        word = 3;
                    }
                }
                reqWords.add(merged.toString().trim());
            }

            //recombines words in the list between a keyword and its value
            if (!combinedWords) {
                for (String key : KEYWORDS.keySet()) {
                    int endPos = rawReqWords.indexOf(KEYWORDS.get(key));

                    if (curWord.contains(key) && endPos != -1) {

                        for (int word = 0; word <= endPos; word++) {
                            merged.append(rawReqWords.removeFirst()).append(" ");
                        }

                        reqWords.add(merged.toString().trim());
                        break;
                    }
                }
            }

            if(!combinedWords && !rawReqWords.isEmpty()){
                reqWords.add(rawReqWords.removeFirst());
            }
        }
        return reqWords;
    }

    /**
     * Filters words to make requisite interpretation easier
     * 
     * @param reqWords the list of words which the filter is applied
     */
    private static void filterReqWords(List<String> reqWords){
        Iterator<String> iter = reqWords.iterator();

        while(iter.hasNext()){
            boolean keepWord = false;
            String curWord = iter.next();

            //Keeps department permission requisites
            if(curWord.contains("PERMISSION")) {
                keepWord = true;

            //Keeps keywords
            }if(!keepWord){
                for(String keyWord: KEYWORDS.keySet()){
                    if(curWord.contains(keyWord) && curWord.contains(KEYWORDS.get(keyWord))){
                        keepWord = true;
                        break;
                    }
                }

            //Keeps conjunctions
            }if(!keepWord){
                
                for(String conjunction: CONJUNCTIONS){
                    if(curWord.matches(conjunction)){
                        keepWord = true;
                        break;
                    }
                }
                
            //Keeps courses
            }if(CourseValidator.isCourseID(curWord)){
                keepWord = true;

            }if(!keepWord){
                iter.remove();
            }
        }
    }

    /**
     * Interprets a list of requisite words into a Set of Requisite Sets
     *
     * @param reqWords the list of words being interpretted
     * @return a formatted Set of Requisite Sets.
     * Course names are converted into CourseReqs while Department
     * permission requisites are converted into DepartmentReqs.  Any other Requisites are converted into OtherReqs.
     * <p>
     * CONJUNCTIONS are used to identify which Requisites are placed in each Requisite Set
     * <p>
     * If there is an "OR" CONJUNCTION, all Requisites before it are added into the Set.  As long as the CONJUNCTION
     * afterwards continues to be the "OR" CONJUNCTION, all Requisites before it will continue to be added.
     * When the condition is not met, the Requisite set will be concluded with the last Requisite after the
     * last "OR" conjunction.
     * <p>
     * If there is an "AND" CONJUNCTION, all Requisites before the CONJUNCTION will have their individual Sets
     * before being added to the main Set.
     */
    private static HashSet<HashSet<Requisite>> interpretReqWords(List<String> reqWords){
        HashSet<HashSet<Requisite>> requisites = new HashSet<>();
        int conjunctionIndex = indexOfConjunction(reqWords);

        while(!reqWords.isEmpty()){
            HashSet<Requisite> singleReqCredit = new HashSet<>();

            if(conjunctionIndex == 0){
                reqWords.remove(0);

            }else if(conjunctionIndex != -1){

                //current CONJUNCTION is "OR"
                if(reqWords.get(conjunctionIndex).equals(CONJUNCTIONS[0])){

                    //continues adding requisites to the singleReqCredit if the only conjunction is "OR"
                    do{
                        //adds all the requisites before "OR"
                        for(int wordCount = 0; wordCount < conjunctionIndex; wordCount++){
                            singleReqCredit.add(RequisiteFactory.getRequisite(reqWords.remove(0)));
                        }
                        reqWords.remove(0);
                        conjunctionIndex = indexOfConjunction(reqWords);

                    }while(conjunctionIndex > 0 && reqWords.get(conjunctionIndex).equals(CONJUNCTIONS[0]));

                    //adds the requisite after "OR" if it exists
                    if(indexOfConjunction(reqWords) != 0 && !reqWords.isEmpty()){
                        singleReqCredit.add(RequisiteFactory.getRequisite(reqWords.remove(0)));
                    }
                    requisites.add(singleReqCredit);

                //current CONJUNCTION is "AND"
                }else if(reqWords.get(conjunctionIndex).equals(CONJUNCTIONS[1])){

                    //adds all the requisites before the "AND" as individual sets
                    for(int wordCount = 0; wordCount < conjunctionIndex; wordCount++){
                        singleReqCredit = new HashSet<>();
                        singleReqCredit.add(RequisiteFactory.getRequisite(reqWords.remove(0)));
                        requisites.add(singleReqCredit);
                    }
                    reqWords.remove(0);

                }

            //No CONJUNCTIONS remaining
            }else{
                singleReqCredit.add(RequisiteFactory.getRequisite(reqWords.remove(0)));
                requisites.add(singleReqCredit);
            }

            conjunctionIndex = indexOfConjunction(reqWords);
        }
        return requisites;
    }

    /**
     * Provides the index of the first instance of a CONJUNCTION in a list
     * @param words the list of words that will be traverse
     * @return the index of the first CONJUNCTION.  If CONJUNCTION doesn't exist, returns -1.
     */
    private static int indexOfConjunction(List<String> words){
        int currentOp = words.indexOf(CONJUNCTIONS[0]);
        int altOp = words.indexOf(CONJUNCTIONS[1]);
        if(currentOp == -1 || (altOp != -1 && altOp < currentOp)){
            currentOp = altOp;
        }
        return currentOp;
    }

}
