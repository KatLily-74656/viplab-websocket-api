package de.uni_stuttgart.tik.viplab.websocket_api.model;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class ComputationResult {

	@NotBlank
	public String identifier;

	public String version;

	/**
	 * The identifier of corresponding Computation
	 */
	@NotBlank
	public String computation;

	public String status;

	public ZonedDateTime timestamp;

	public Output output;

	public List<Artifact> artifacts = Collections.emptyList();


	public static class Output {
		public String stdout;
		public String stderr;
	}

	public static class Artifact {

		enum TYPE {
			notifications,
			file,
			s3file
		}

		public String identifier;
		public TYPE type;
	}

	public static class Notifications extends Artifact{

		public Notifications() {
			type = TYPE.notifications;
		}

		public String summary;

		public List<Notification> notifications = Collections.emptyList();

		public static class Notification {
			public String severity;
			public String type;
			public String message;
		}
	}

	public static class File extends Artifact{

		public File() {
			type = TYPE.file;
		}

		public String path;
		public String MIMEtype;
		public String content;
	}

  public static class S3File extends Artifact {
    public S3File() {
      type = TYPE.s3file;
    }

    public String path;
    public String MIMEtype;
    public URL url;
    public long size;
    public String hash;

  }
}
