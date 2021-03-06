package prog12;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class PageTrie extends TreeMap<String, Long> {

	private DirectoryTrie<PageFile> trie;

	private static final String DIRECTORY = "page.dir";

	public PageTrie() {
		try {
			trie = new DirectoryTrie<PageFile>(DIRECTORY);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public void write(HardDisk<PageFile> pageDisk) {
		for (String s : keySet()) {
			trie.add(s, pageDisk.get(get(s)));
		}
	}

	public void read(HardDisk<PageFile> pageDisk) {
		List<String> list = trie.traverse();
		for (String s : list) {
			String[] sa = s.split("[\\(\\)]");
			if (sa.length == 3) {
				long index = Long.parseLong(sa[0]);

				pageDisk.newFile(); // everytime we read a file from disk,
									// pretend like we've created a new
									// file, that way if we index more pages
									// along with the pages from disk,
									// we don't get duplicate indices

				String url = sa[1];
				int refcounts = Integer.parseInt(sa[2]);
				put(url, index);
				PageFile pf = new PageFile(index, url);
				pf.setRefCount(refcounts);
				pageDisk.put(index, pf);
			}
		}
	}

}
