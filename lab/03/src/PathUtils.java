public class PathUtils {
    public static void main(String[] args) {
        System.out.println(getCanonicalPath("/home/"));
        System.out.println(getCanonicalPath("/../"));
        System.out.println(getCanonicalPath("/home//foo/"));
        System.out.println(getCanonicalPath("/a/./b/../../c/"));
    }

    public static String getCanonicalPath(String path) {
        String[] pathArr = path.replaceAll("/+", "/").split("/");
        pathArr[0] = "/";
        int j = 0;

        StringBuilder canonicalPath = new StringBuilder();

        for (int i = 0; i < pathArr.length; ++i) {
            if (pathArr[i].equals("..")) {
                if (j - 1 > 0) {
                    --j;
                } else {
                    j = 0;
                }
            } else if (!pathArr[i].equals(".")) {
                pathArr[j++] = pathArr[i];
            }
        }

        int i = 0;
        do {
            canonicalPath.append(pathArr[i]);

            if (i > 0 && i < j - 1) {
                canonicalPath.append("/");
            }

            ++i;
        } while (i < j);

        return canonicalPath.toString();
    }
}
