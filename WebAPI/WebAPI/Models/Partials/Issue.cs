using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Issue
    {
        public static IssueDTO ToDTO(Issue item, int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new IssueDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Request = Request.ToDTO(item.Request, level - 1),
                RequestId = item.RequestId,
                Price = item.Price,
                Accepted = item.Accepted,
                Description = item.Description
            };
        }

        public static ICollection<IssueDTO> ToListDTO(ICollection<Issue> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public IssueDTO ToDTO(int level)
        {
            return level > 0 ? new IssueDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Request = Request.ToDTO(Request, level - 1),
                RequestId = RequestId,
                Price = Price,
                Accepted = Accepted,
                Description = Description
            } : null;
        }
    }
}