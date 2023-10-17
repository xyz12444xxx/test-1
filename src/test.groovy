// List<List<String>> splitListIntoGroups(List<String> a1, int groupSize) {
//     List<List<String>> result = []
//     int startIndex = 0
    
//     while (startIndex < a1.size()) {
//         int endIndex = Math.min(startIndex + groupSize, a1.size())
//         List<String> group = a1.subList(startIndex, endIndex)
//         result.add(group)
//         startIndex += groupSize
//     }
    
//     return result
// }

// // Example usage:
// List<String> a1 = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16']

// List<List<String>> groupedLists = splitListIntoGroups(a1, 5)

// groupedLists.each { group ->
//     println(group)
// }


def somefn(String val) {
    println val
}

Map mapper = [:]

mapper["1"] = {somefn("something1")}
mapper["2"] = {somefn("something2")}
mapper["3"] = {somefn("something3")}

mapper.each { key, fn -> 
    fn()
}