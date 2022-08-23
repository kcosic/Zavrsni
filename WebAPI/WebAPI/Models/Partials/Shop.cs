using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Shop{
        public static ShopDTO ToDTO(Shop item, bool singleLevel = true)
        {
            return new ShopDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Location = singleLevel ? null : Location.ToDTO(item.Location),
                RepairHistories = singleLevel ? null : RepairHistory.ToListDTO(item.RepairHistories),
                Requests = singleLevel ? null : Request.ToListDTO(item.Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(item.Reviews),
                Appointments = singleLevel ? null : Appointment.ToListDTO(item.Appointments),
                LegalName = item.LegalName,
                LocationId= item.LocationId,
                ShortName= item.ShortName
            };
        }

        public static ICollection<ShopDTO> ToListDTO(ICollection<Shop> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public ShopDTO ToDTO(bool singleLevel = true)
        {
            return new ShopDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Location = singleLevel ? null : Location.ToDTO(Location),
                RepairHistories = singleLevel ? null : RepairHistory.ToListDTO(RepairHistories),
                Requests = singleLevel ? null : Request.ToListDTO(Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(Reviews),
                Appointments = singleLevel ? null : Appointment.ToListDTO(Appointments),
                LegalName = LegalName,
                LocationId = LocationId,
                ShortName = ShortName
            };
        }
    }

}