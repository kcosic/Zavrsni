using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using WebAPI.Models.Enums;
using WebAPI.Models.ORM;

namespace WebAPI.Models
{
    public abstract class BaseController : ApiController
    {
        protected readonly string _controllerName;
        protected readonly DbEntities _db;
        protected User _user;

        public User AuthUser { 
            get {

                if (_user != null)
                {
                    return _user;
                }

                if (_user == null && User?.Identity?.Name != null)
                {
                    
                    _user = _db.Users.Where(u => u.Username == User.Identity.Name).FirstOrDefault();
                }

                return null;
            } 
        }

        public DbEntities Db {
            get
            {
                return _db;
            }
            
        }

        public BaseController(string controllerName)
        {
            _controllerName = controllerName;
            _db = new DbEntities();
        }


        private void Log(SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(AuthUser.Id, ApplicationEnum.API, severity, _controllerName, message, timestamp);

        }        
        
        private void Log(ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(AuthUser.Id, application, severity, _controllerName, message, timestamp);

        }

        private void Log(int userId, ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, application, severity, _controllerName, message, timestamp);

        }

        private void Log(int userId, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, ApplicationEnum.API, severity, _controllerName, message, timestamp);
        }

        private void Log(int userId, ApplicationEnum application, SeverityEnum severity, string source, string message, DateTime timestamp)
        {
            Db.Logs.Add(new Log
            {
                UserId = userId,
                Application = Enum.GetName(typeof(ApplicationEnum), application),
                Severity = Enum.GetName(typeof(SeverityEnum), severity),
                Source = source.Length > 200 ? source.Substring(0, 199) : source,
                Message = message,
                Timestamp = timestamp
            });
            Db.SaveChanges();
        }
    }
}