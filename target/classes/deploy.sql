CREATE TABLE `users` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(20) NOT NULL,
    `coins` int(11) NOT NULL DEFAULT '0',
    `exp` int(11) NOT NULL DEFAULT '0',
    `status` varchar(20) DEFAULT NULL,
    `statusto` int(11) DEFAULT NULL,
    `donated` int(11) NOT NULL DEFAULT '0',
    `online` int(11) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `usernames` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

CREATE TABLE `servers` (
    `id` varchar(16) NOT NULL DEFAULT '',
    `host` varchar(50) NOT NULL,
    `port` int(11) NOT NULL DEFAULT '25565',
    `online` int(10) unsigned DEFAULT NULL,
    `max` int(11) unsigned DEFAULT NULL,
    `connectable` tinyint(1) DEFAULT '1',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reports` (
  `username` varchar(20) NOT NULL,
  `target` varchar(20) NOT NULL,
  `type` varchar(20) NOT NULL,
  `message` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`username`, `target`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `users_meta` (
  `userid` int(11) NOT NULL DEFAULT '0',
  `key` varchar(30) NOT NULL DEFAULT '',
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

CREATE TABLE `metrics` (
  `id` varchar(40) NOT NULL,
  `value` int(11) NOT NULL DEFAULT '0',
  `last` int(11) NOT NULL DEFAULT '0',
  `total` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stream_channels` (
  `username` varchar(20) NOT NULL,
  `channel` varchar(255) NOT NULL,
  PRIMARY KEY (`username`,`channel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
