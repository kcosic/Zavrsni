using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{    partial class Log {
        public static LogDTO ToDTO(Log item, bool singleLevel = true)
        {
            return new LogDTO
            {
                Id = item.Id,
                UserId = item.UserId,
                Application = item.Application,
                Message = item.Message,
                Severity = item.Severity,
                Source = item.Source,
                Timestamp = item.Timestamp
            };
        }

        public static ICollection<LogDTO> ToListDTO(ICollection<Log> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public LogDTO ToDTO(bool singleLevel = true)
        {
            return new LogDTO
            {
                Id = Id,
                UserId = UserId,
                Application = Application,
                Message = Message,
                Severity = Severity,
                Source = Source,
                Timestamp = Timestamp
            };
        }
    }
}