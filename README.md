# yt-downloader

YT-downloader is a JEE api for youtube-dl. It allows the user to download music or videos using youtube-dl within a browser. Application was designed for wildfly 10.1.0 to run as a windows service. Although it preforms session creation for every usage using this app to serve more than one client at a time is not supported. Application always store the files in previously set direcitories which could be different for videos and music and do not pass this files back to a client.

Requirements:

- youtube-dl and ffmpeg installed in system environment variables.
- wildfly 10.1.0 or newer.
- maven and java for build

Default context root is host:port/YT/
