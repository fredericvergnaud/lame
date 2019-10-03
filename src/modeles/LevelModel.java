package modeles;

import java.util.ArrayList;

public class LevelModel {

	private Long id;
	private String type, url;
	private ArrayList<Row> rows = new ArrayList<Row>();
	private Row row;
	private Pagination pagination;

	public ArrayList<Row> getRows() {
		return rows;
	}

	public void setRows(ArrayList<Row> rows) {
		this.rows = rows;
	}

	public LevelModel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long jsonLevelId) {
		this.id = jsonLevelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Row getNewRow() {
		this.row = new Row();
		return row;
	}

	public Pagination getNewPagination() {
		this.pagination = new Pagination();
		return pagination;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public class Pagination {

		private String tagName, className, xHref;
		private int interval, lastPage;
		private ArrayList<String> paginationUrls;

		public String getxHref() {
			return xHref;
		}

		public void setxHref(String xHref) {
			this.xHref = xHref;
		}

		public String getTagName() {
			return tagName;
		}

		public int getInterval() {
			return interval;
		}

		public void setInterval(Long interval) {
			this.interval = interval.intValue();
		}

		public int getLastPage() {
			return lastPage;
		}

		public void setLastPage(Long lastPage) {
			this.lastPage = lastPage.intValue();
		}

		public void setTagName(String tagName) {
			this.tagName = tagName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public ArrayList<String> getPaginationUrls() {
			return paginationUrls;
		}

		public void setPaginationUrls(ArrayList<String> paginationUrls) {
			this.paginationUrls = paginationUrls;
		}

	}

	public class Row {

		private int id;
		private String tagName, className;
		private ArrayList<Col> cols = new ArrayList<Col>();
		private Depth depth;
		private Col col;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTagName() {
			return tagName;
		}

		public void setTagName(String tagName) {
			this.tagName = tagName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public ArrayList<Col> getCols() {
			return cols;
		}

		public void setCols(ArrayList<Col> cols) {
			this.cols = cols;
		}

		public Depth getDepth() {
			return depth;
		}

		public void setDepth(Depth depth) {
			this.depth = depth;
		}

		public Col getNewCol() {
			this.col = new Col();
			return col;
		}

		public Depth getNewDepth() {
			this.depth = new Depth();
			return depth;
		}

		public class Col {
			private int id;
			private String title, tagName, className;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getTagName() {
				return tagName;
			}

			public void setTagName(String tagName) {
				this.tagName = tagName;
			}

			public String getClassName() {
				return className;
			}

			public void setClassName(String className) {
				this.className = className;
			}

		}

		public class Depth {
			private int id;
			private String tagName, className;
			private ArrayList<String> linksArray = new ArrayList<String>();

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getTagName() {
				return tagName;
			}

			public void setTagName(String tagName) {
				this.tagName = tagName;
			}

			public String getClassName() {
				return className;
			}

			public void setClassName(String className) {
				this.className = className;
			}

			public ArrayList<String> getLinksArray() {
				return linksArray;
			}

			public void setLinksArray(ArrayList<String> linksArray) {
				this.linksArray = linksArray;
			}

		}
	}

}