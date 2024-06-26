﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{    partial class Log {
        public static LogDTO ToDTO(Log item)
        {
            if (item == null)
            {
                return null;
            }

            return new LogDTO
            {
                Id = item.Id,
                UserId = item.UserId,
                ShopId = item.ShopId,
                Application = item.Application,
                Message = item.Message,
                Severity = item.Severity,
                Source = item.Source,
                Timestamp = item.Timestamp
            };
        }

        public static ICollection<LogDTO> ToListDTO(ICollection<Log> list)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO()).ToList();
        }

        public LogDTO ToDTO()
        {
            return new LogDTO
            {
                Id = Id,
                UserId = UserId,
                ShopId = ShopId,
                Application = Application,
                Message = Message,
                Severity = Severity,
                Source = Source,
                Timestamp = Timestamp
            };
        }
    }
}